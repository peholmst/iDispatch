package net.pkhapps.idispatch.gis.importer;

import com.vividsolutions.jts.geom.*;
import net.pkhapps.idispatch.gis.domain.model.*;
import net.pkhapps.idispatch.gis.domain.model.identity.MaterialImportId;
import net.pkhapps.idispatch.gis.domain.model.identity.MunicipalityId;
import net.pkhapps.idispatch.shared.domain.base.BaseRepository;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;

import javax.xml.namespace.QName;
import java.io.File;
import java.sql.Date;
import java.time.Clock;
import java.util.*;
import java.util.stream.Stream;

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

    private final RoadSegmentRepository roadSegmentRepository;
    private final AddressPointRepository addressPointRepository;
    private final MunicipalityRepository municipalityRepository;

    private Map<Integer, MunicipalityId> municipalityCodeCache;

    public TerrainDatabaseImporter(@NotNull Clock clock,
                                   @NotNull PlatformTransactionManager platformTransactionManager,
                                   @NotNull MaterialImportRepository materialImportRepository,
                                   @NotNull RoadSegmentRepository roadSegmentRepository,
                                   @NotNull AddressPointRepository addressPointRepository,
                                   @NotNull MunicipalityRepository municipalityRepository) {
        super(clock, platformTransactionManager, materialImportRepository,
                "http://xml.nls.fi/XML/Namespace/Maastotietojarjestelma/SiirtotiedostonMalli/2011-02",
                "http://xml.nls.fi/XML/Schema/Maastotietojarjestelma/MTK/201405/Maastotiedot.xsd");
        this.roadSegmentRepository = roadSegmentRepository;
        this.addressPointRepository = addressPointRepository;
        this.municipalityRepository = municipalityRepository;
    }

    @Override
    public void importData(@Nullable File file) {
        buildMunicipalityCodeCache();
        super.importData(file);
    }

    private void buildMunicipalityCodeCache() {
        municipalityCodeCache = new HashMap<>();
        municipalityRepository.findAll().forEach(m -> municipalityCodeCache.put(m.code(), m.id()));
    }

    @Override
    protected @NotNull Stream<Importer> importers() {
        return Stream.of(new RoadSegmentImporter(), new AddressPointImporter());
    }

    private <T> @NotNull T getRequired(@NotNull Map<String, Object> map, @NotNull String key, @NotNull Class<T> type) {
        return getOptional(map, key, type).orElseThrow(() -> new IllegalArgumentException("Missing required attribute: " + key));
    }

    private <T> @NotNull Optional<T> getOptional(@NotNull Map<String, Object> map, @NotNull String key, @NotNull Class<T> type) {
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

    private @Nullable String mapName(@NotNull Object name) {
        if (name instanceof Map) {
            return (String) ((Map) name).get(null);
        } else {
            return null;
        }
    }

    private void convertToTwoDimensions(@NotNull Geometry geometry) {
        geometry.apply((CoordinateFilter) coord -> {
            if (coord.z != Coordinate.NULL_ORDINATE) {
                coord.setCoordinate(new Coordinate(coord.x, coord.y));
            }
        });
        geometry.geometryChanged();
    }

    // TODO Checking for existing stuff by GID will result in elements with duplicate GIDs being skipped completely.
    // This has to be fixed in some way. Maybe compute a check-sum to check the contents?

    private abstract class BaseImporter<T extends ImportedGeographicalMaterial<?, ?>> implements Importer {

        private final QName featureType;
        private final BaseRepository<?, ?, T> repository;

        BaseImporter(@NotNull QName featureType, BaseRepository<?, ?, T> repository) {
            this.featureType = featureType;
            this.repository = repository;
        }

        @Override
        public @NotNull QName featureType() {
            return featureType;
        }

        @Override
        public void importFeatures(@NotNull Iterator<Map<String, Object>> featureIterator, @NotNull MaterialImportId materialImportId) {
            var entitiesToUpdate = new ArrayList<T>();
            logger().info("Importing features of type {}", featureType);
            long importedCount = 0;
            long skippedCount = 0;
            while (featureIterator.hasNext()) {
                var feature = featureIterator.next();
                var entity = createEntity(feature, materialImportId);
                if (entity.isPresent()) {
                    importedCount++;
                    entitiesToUpdate.add(entity.get());
                } else {
                    skippedCount++;
                }
                if (entitiesToUpdate.size() == 50) {
                    repository.saveAll(entitiesToUpdate);
                    importedCount += entitiesToUpdate.size();
                    entitiesToUpdate.clear();
                }
            }
            repository.saveAll(entitiesToUpdate);
            importedCount += entitiesToUpdate.size();
            logger().info("Imported {} features, skipped {}", importedCount, skippedCount);
        }

        abstract @NotNull Optional<T> createEntity(@NotNull Map<String, Object> feature,
                                                   @NotNull MaterialImportId materialImportId);
    }

    private class RoadSegmentImporter extends BaseImporter<RoadSegment> {

        RoadSegmentImporter() {
            super(createFeatureTypeName("Tieviiva"), roadSegmentRepository);
        }

        @Override
        @NotNull Optional<RoadSegment> createEntity(@NotNull Map<String, Object> feature,
                                                    @NotNull MaterialImportId materialImportId) {
            var gid = getRequired(feature, "gid", Long.class);
            var locationAccuracy = Code.findByCode(LocationAccuracy.class, getRequired(feature, "sijaintitarkkuus", String.class));
            var location = getRequired(feature, "sijainti", LineString.class);
            location.setSRID(3067); // NLS material use this SRID
            convertToTwoDimensions(location);

            var elevation = Code.findByCode(Elevation.class, getRequired(feature, "tasosijainti", String.class));
            var roadNumber = getOptional(feature, "tienumero", Long.class);
            var nameFin = getOptional(feature, "nimi_suomi", Object.class).map(TerrainDatabaseImporter.this::mapName);
            var nameSwe = getOptional(feature, "nimi_ruotsi", Object.class).map(TerrainDatabaseImporter.this::mapName);

            if (nameFin.isEmpty()) {
                nameFin = nameSwe;
            }
            if (nameSwe.isEmpty()) {
                nameSwe = nameFin;
            }

            var municipalityCode = Integer.parseInt(getRequired(feature, "kuntatunnus", String.class));

            var minAddressNumberLeft = getOptional(feature, "minOsoitenumeroVasen", Integer.class);
            var maxAddressNumberLeft = getOptional(feature, "maxOsoitenumeroVasen", Integer.class);
            var minAddressNumberRight = getOptional(feature, "minOsoitenumeroOikea", Integer.class);
            var maxAddressNumberRight = getOptional(feature, "maxOsoitenumeroOikea", Integer.class);
            var validFrom = getRequired(feature, "alkupvm", Date.class).toLocalDate();
            var validTo = getOptional(feature, "loppupvm", Date.class).map(Date::toLocalDate);
            var municipalityId = municipalityCodeCache.get(municipalityCode);
            if (municipalityId == null) {
                logger().warn("Could not find municipality with code {}", municipalityCode);
                return Optional.empty();
            }

            if (!roadSegmentRepository.existsByGid(gid)) {
                logger().trace("Creating new RoadSegment for GID {}", gid);
                var segment = new RoadSegment(gid, locationAccuracy, location, elevation, municipalityId,
                        validFrom, materialImportId);
                roadNumber.ifPresent(segment::setRoadNumber);
                nameFin.ifPresent(segment::setNameFin);
                nameSwe.ifPresent(segment::setNameSwe);
                minAddressNumberLeft.ifPresent(segment::setMinAddressNumberLeft);
                maxAddressNumberLeft.ifPresent(segment::setMaxAddressNumberLeft);
                minAddressNumberRight.ifPresent(segment::setMinAddressNumberRight);
                maxAddressNumberRight.ifPresent(segment::setMaxAddressNumberRight);
                validTo.ifPresent(segment::setValidTo);
                return Optional.of(segment);
            } else {
                logger().trace("RoadSegment for GID {} already exists in the database, skipping", gid);
                return Optional.empty();
            }
        }
    }

    private class AddressPointImporter extends BaseImporter<AddressPoint> {

        AddressPointImporter() {
            super(createFeatureTypeName("Osoitepiste"), addressPointRepository);
        }

        @Override
        @NotNull Optional<AddressPoint> createEntity(@NotNull Map<String, Object> feature, @NotNull MaterialImportId materialImportId) {
            var gid = getRequired(feature, "gid", Long.class);
            var locationAccuracy = Code.findByCode(LocationAccuracy.class, getRequired(feature, "sijaintitarkkuus", String.class));
            var location = getRequired(feature, "sijainti", Point.class);
            location.setSRID(3067); // NLS material use this SRID
            convertToTwoDimensions(location);

            var number = getOptional(feature, "numero", String.class);
            var nameFin = getOptional(feature, "nimi_suomi", Object.class).map(TerrainDatabaseImporter.this::mapName);
            var nameSwe = getOptional(feature, "nimi_ruotsi", Object.class).map(TerrainDatabaseImporter.this::mapName);

            if (nameFin.isEmpty()) {
                nameFin = nameSwe;
            }
            if (nameSwe.isEmpty()) {
                nameSwe = nameFin;
            }

            var municipalityCode = Integer.parseInt(getRequired(feature, "kuntatunnus", String.class));
            var validFrom = getRequired(feature, "alkupvm", Date.class).toLocalDate();
            var validTo = getOptional(feature, "loppupvm", Date.class).map(Date::toLocalDate);
            var municipalityId = municipalityCodeCache.get(municipalityCode);

            if (!addressPointRepository.existsByGid(gid)) {
                logger().trace("Creating new AddressPoint for GID {}", gid);
                var addressPoint = new AddressPoint(gid, locationAccuracy, location, municipalityId,
                        validFrom, materialImportId);
                number.ifPresent(addressPoint::setNumber);
                nameFin.ifPresent(addressPoint::setNameFin);
                nameSwe.ifPresent(addressPoint::setNameSwe);
                validTo.ifPresent(addressPoint::setValidTo);
                return Optional.of(addressPoint);
            } else {
                logger().trace("AddressPoint for GID {} already exists in the database, skipping", gid);
                return Optional.empty();
            }
        }
    }
}
