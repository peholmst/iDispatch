package net.pkhapps.idispatch.gis.importer;

import com.vividsolutions.jts.geom.*;
import net.pkhapps.idispatch.gis.domain.model.*;
import net.pkhapps.idispatch.gis.domain.model.identity.MunicipalityId;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Date;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

import static javax.xml.stream.XMLStreamConstants.START_ELEMENT;

/**
 * Importer that imports data from the terrain database "Maastotietokanta". You have to download the material from the
 * NLS web site and point the importer to the directory containing the GML files.
 * <p>
 * Currently imports:
 * <ul>
 * <li>{@link RoadSegment}s</li>
 * <li>{@link AddressPoint}s</li>
 * </ul>
 */
@Component
public class TerrainDatabaseImporter extends GmlFileImporter {

    private final RoadSegmentImportService roadSegmentImportService;
    private final AddressPointImportService addressPointImportService;

    public TerrainDatabaseImporter(@NotNull PlatformTransactionManager platformTransactionManager,
                                   @NotNull RoadSegmentImportService roadSegmentImportService,
                                   @NotNull AddressPointImportService addressPointImportService) {
        super(platformTransactionManager,
                "http://xml.nls.fi/XML/Namespace/Maastotietojarjestelma/SiirtotiedostonMalli/2011-02",
                "http://xml.nls.fi/XML/Schema/Maastotietojarjestelma/MTK/201405/Maastotiedot.xsd");
        this.roadSegmentImportService = roadSegmentImportService;
        this.addressPointImportService = addressPointImportService;
    }

    private static <T> @NotNull T getRequired(@NotNull Map<String, Object> map, @NotNull String key, @NotNull Class<T> type) {
        return getOptional(map, key, type).orElseThrow(() -> new IllegalArgumentException("Missing required attribute: " + key));
    }

    private static <T> @NotNull Optional<T> getOptional(@NotNull Map<String, Object> map, @NotNull String key, @NotNull Class<T> type) {
        var value = map.get(key);
        if (value != null) {
            if (type.isInstance(value)) {
                return Optional.of(type.cast(value));
            } else {
                throw new IllegalArgumentException("Attribute (" + key + ") value " + value + " is not of type " + type.getName() + " but of type " + value.getClass().getName());
            }
        }
        return Optional.empty();
    }

    private static @Nullable String mapName(@NotNull Object name) {
        if (name instanceof Map) {
            return (String) ((Map) name).get(null);
        } else {
            return null;
        }
    }

    private static void convertToTwoDimensions(@NotNull Geometry geometry) {
        geometry.apply((CoordinateFilter) coord -> {
            if (coord.z != Coordinate.NULL_ORDINATE) {
                coord.setCoordinate(new Coordinate(coord.x, coord.y));
            }
        });
        geometry.geometryChanged();
    }

    @Override
    protected @NotNull Stream<Importer> importers() {
        return Stream.of(new RoadSegmentImporter(), new AddressPointImporter());
    }

    @Override
    protected @NotNull Instant extractMaterialTimestamp(@NotNull File file) {
        XMLInputFactory inputFactory = XMLInputFactory.newFactory();
        try (InputStream is = new FileInputStream(file)) {
            XMLStreamReader reader = inputFactory.createXMLStreamReader(is);
            int eventType = reader.next();
            if (eventType == START_ELEMENT && reader.getLocalName().equals("Maastotiedot")) {
                String timestamp = reader.getAttributeValue(null, "aikaleima");
                if (timestamp != null) {
                    return ZonedDateTime.parse(timestamp).toInstant();
                }
            }
        } catch (IOException | XMLStreamException ex) {
            throw new RuntimeException("Error reading from " + file, ex);
        }
        throw new IllegalArgumentException("No timestamp found in " + file);
    }

    private abstract class BaseImporter<T extends ImportedGeographicalMaterial> implements Importer<T> {

        private final QName featureType;
        private final ImportedGeographicalMaterialImportService<T> importService;

        BaseImporter(@NotNull QName featureType, @NotNull ImportedGeographicalMaterialImportService<T> importService) {
            this.featureType = featureType;
            this.importService = importService;
        }

        @Override
        public @NotNull QName featureType() {
            return featureType;
        }

        @Override
        public @NotNull ImportedGeographicalMaterialImportService<T> importService() {
            return importService;
        }
    }

    private class RoadSegmentImporter extends BaseImporter<RoadSegment> {

        RoadSegmentImporter() {
            super(createFeatureTypeName("Tieviiva"), roadSegmentImportService);
        }

        @Override
        public @NotNull RoadSegment mapFeature(@NotNull Map<String, Object> feature) {
            var gid = getRequired(feature, "gid", Long.class);
            var locationAccuracy = Code.findByCode(LocationAccuracy.class, getRequired(feature, "sijaintitarkkuus", String.class));
            var location = getRequired(feature, "sijainti", LineString.class);
            location.setSRID(3067); // NLS material use this SRID
            convertToTwoDimensions(location);

            var elevation = Code.findByCode(Elevation.class, getRequired(feature, "tasosijainti", String.class));

            var roadNumber = getOptional(feature, "tienumero", Long.class);
            var nameFin = getOptional(feature, "nimi_suomi", Object.class).map(TerrainDatabaseImporter::mapName);
            var nameSwe = getOptional(feature, "nimi_ruotsi", Object.class).map(TerrainDatabaseImporter::mapName);

            if (nameFin.isEmpty()) {
                nameFin = nameSwe;
            }
            if (nameSwe.isEmpty()) {
                nameSwe = nameFin;
            }

            var municipality = new MunicipalityId(Integer.parseInt(getRequired(feature, "kuntatunnus", String.class)));

            var minAddressNumberLeft = getOptional(feature, "minOsoitenumeroVasen", Integer.class);
            var maxAddressNumberLeft = getOptional(feature, "maxOsoitenumeroVasen", Integer.class);
            var minAddressNumberRight = getOptional(feature, "minOsoitenumeroOikea", Integer.class);
            var maxAddressNumberRight = getOptional(feature, "maxOsoitenumeroOikea", Integer.class);
            var validFrom = getRequired(feature, "alkupvm", Date.class).toLocalDate();
            var validTo = getOptional(feature, "loppupvm", Date.class).map(Date::toLocalDate);

            return new RoadSegment(gid,
                    locationAccuracy,
                    location,
                    elevation,
                    roadNumber.orElse(null),
                    nameFin.orElse(null),
                    nameSwe.orElse(null),
                    municipality,
                    minAddressNumberLeft.orElse(null),
                    maxAddressNumberLeft.orElse(null),
                    minAddressNumberRight.orElse(null),
                    maxAddressNumberRight.orElse(null),
                    validFrom,
                    validTo.orElse(null));
        }
    }

    private class AddressPointImporter extends BaseImporter<AddressPoint> {

        AddressPointImporter() {
            super(createFeatureTypeName("Osoitepiste"), addressPointImportService);
        }

        @Override
        public @NotNull AddressPoint mapFeature(@NotNull Map<String, Object> feature) {
            var gid = getRequired(feature, "gid", Long.class);
            var locationAccuracy = Code.findByCode(LocationAccuracy.class, getRequired(feature, "sijaintitarkkuus", String.class));
            var location = getRequired(feature, "sijainti", Point.class);
            location.setSRID(3067); // NLS material use this SRID
            convertToTwoDimensions(location);

            var number = getOptional(feature, "numero", String.class);
            var nameFin = getOptional(feature, "nimi_suomi", Object.class).map(TerrainDatabaseImporter::mapName);
            var nameSwe = getOptional(feature, "nimi_ruotsi", Object.class).map(TerrainDatabaseImporter::mapName);

            if (nameFin.isEmpty()) {
                nameFin = nameSwe;
            }
            if (nameSwe.isEmpty()) {
                nameSwe = nameFin;
            }

            var municipality = new MunicipalityId(Integer.parseInt(getRequired(feature, "kuntatunnus", String.class)));

            var validFrom = getRequired(feature, "alkupvm", Date.class).toLocalDate();
            var validTo = getOptional(feature, "loppupvm", Date.class).map(Date::toLocalDate);

            return new AddressPoint(gid,
                    locationAccuracy,
                    location,
                    number.orElse(null),
                    nameSwe.orElse(null),
                    nameFin.orElse(null),
                    municipality,
                    validFrom,
                    validTo.orElse(null));
        }
    }
}
