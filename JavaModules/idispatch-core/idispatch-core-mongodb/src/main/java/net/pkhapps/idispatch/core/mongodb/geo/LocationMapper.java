package net.pkhapps.idispatch.core.mongodb.geo;

import net.pkhapps.idispatch.core.domain.geo.*;
import net.pkhapps.idispatch.core.mongodb.i18n.MultilingualStringMapper;
import org.bson.Document;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Optional;

/**
 * Mapper that maps between {@link Location} and {@link Document}.
 */
public class LocationMapper {

    private static final String KEY_LOCATION_EASTING = "easting";
    private static final String KEY_LOCATION_NORTHING = "northing";
    private static final String KEY_POSITION = "position";
    private static final String KEY_MUNICIPALITY = "municipality";
    private static final String KEY_ADDITIONAL_DETAILS = "additionalDetails";
    private static final String KEY_TYPE = "type";
    private static final String KEY_ROAD_NAME = "roadName";
    private static final String KEY_INTERSECTING_ROAD_NAME = "intersectingRoadName";
    private static final String KEY_NUMBER = "number";
    private static final String KEY_APARTMENT = "apartment";

    private final MultilingualStringMapper multilingualStringMapper = new MultilingualStringMapper();

    /**
     * Converts the given location to a BSON document.
     *
     * @param location the location to convert.
     * @return the converted document or {@code null} if the location was {@code null}.
     */
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
        if (location instanceof Intersection) {
            document.put(KEY_INTERSECTING_ROAD_NAME, multilingualStringMapper.toDocument(((Intersection) location).intersectingRoadName()));
        }
        if (location instanceof StreetAddress) {
            document.put(KEY_NUMBER, ((StreetAddress) location).number().orElse(null));
            document.put(KEY_APARTMENT, ((StreetAddress) location).apartment().orElse(null));
        }
        return document;
    }

    /**
     * Converts the given BSON document to a location.
     *
     * @param document the document to convert.
     * @return the location or {@code null} if the document was {@code null}.
     */
    @Contract("null -> null")
    public Location toLocation(Document document) {
        if (document == null) {
            return null;
        }
        var position = toPosition(document.get(KEY_POSITION, Document.class));
        var municipality = Optional.ofNullable(document.getString(KEY_MUNICIPALITY)).map(MunicipalityId::new).orElse(null);
        var additionalDetails = document.getString(KEY_ADDITIONAL_DETAILS);
        var type = document.getString(KEY_TYPE);

        // TODO Refactor this into something more object-oriented if needed in the future
        if (RoadLocation.class.getSimpleName().equals(type)) {
            var roadName = multilingualStringMapper.toMultilingualString(document.get(KEY_ROAD_NAME, Document.class));
            return new RoadLocation(position, municipality, additionalDetails, roadName);
        } else if (Intersection.class.getSimpleName().equals(type)) {
            var roadName = multilingualStringMapper.toMultilingualString(document.get(KEY_ROAD_NAME, Document.class));
            var intersectingRoadName = multilingualStringMapper.toMultilingualString(document.get(
                    KEY_INTERSECTING_ROAD_NAME, Document.class));
            return new Intersection(position, municipality, additionalDetails, roadName, intersectingRoadName);
        } else if (StreetAddress.class.getSimpleName().equals(type)) {
            var roadName = multilingualStringMapper.toMultilingualString(document.get(KEY_ROAD_NAME, Document.class));
            var number = document.getString(KEY_NUMBER);
            var apartment = document.getString(KEY_APARTMENT);
            return new StreetAddress(position, municipality, additionalDetails, roadName, number, apartment);
        } else if (UnnamedLocation.class.getSimpleName().equals(type)) {
            return new UnnamedLocation(position, municipality, additionalDetails);
        }

        throw new IllegalArgumentException("Unknown location type: " + type);
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

    private @NotNull Position toPosition(@NotNull Document document) {
        return Position.createFromTm35Fin(document.getDouble(KEY_LOCATION_EASTING),
                document.getDouble(KEY_LOCATION_NORTHING));
    }
}
