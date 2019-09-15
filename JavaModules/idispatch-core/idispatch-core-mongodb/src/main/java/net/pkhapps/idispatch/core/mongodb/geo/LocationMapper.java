package net.pkhapps.idispatch.core.mongodb.geo;

import net.pkhapps.idispatch.core.domain.geo.Location;
import net.pkhapps.idispatch.core.domain.geo.MunicipalityId;
import net.pkhapps.idispatch.core.domain.geo.Position;
import net.pkhapps.idispatch.core.domain.geo.RoadLocation;
import net.pkhapps.idispatch.core.mongodb.i18n.MultilingualStringMapper;
import org.bson.Document;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * TODO Document me
 */
public class LocationMapper {

    private static final String KEY_POSITION = "position";
    private static final String KEY_MUNICIPALITY = "municipality";
    private static final String KEY_ADDITIONAL_DETAILS = "additionalDetails";
    private static final String KEY_TYPE = "type";
    private static final String KEY_ROAD_NAME = "roadName";

    private final MultilingualStringMapper multilingualStringMapper = new MultilingualStringMapper();

    @Contract("null -> null")
    public Document toDocument(Location location) {
        if (location == null) {
            return null;
        }
        var document = new Document();
        document.put(KEY_POSITION, toDocument(location.coordinates()));
        document.put(KEY_MUNICIPALITY, location.municipality().map(MunicipalityId::toString).orElse(null));
        document.put(KEY_ADDITIONAL_DETAILS, location.additionalDetails().orElse(null));
        document.put(KEY_TYPE, location.getClass().getSimpleName());

        // TODO Refactor this into something more object-oriented if needed in the future
        if (location instanceof RoadLocation) {
            document.put(KEY_ROAD_NAME, multilingualStringMapper.toDocument(((RoadLocation) location).roadName()));
        }

        return document;
    }

    private @NotNull Document toDocument(@NotNull Position position) {
        var document = new Document();
        document.append("easting", position.getEasting()).append("northing", position.getNorthing());
        // MongoDB uses WGS84 for all its geospatial queries
        var wgs84 = position.toWgs84();
        document.append("wgs84", new Document()
                .append("type", "Point")
                .append("coordinates", List.of(wgs84.getX(), wgs84.getY()))
        );
        return document;
    }

    @Contract("null -> null")
    public Location toLocation(Document document) {
        return null;
    }

}
