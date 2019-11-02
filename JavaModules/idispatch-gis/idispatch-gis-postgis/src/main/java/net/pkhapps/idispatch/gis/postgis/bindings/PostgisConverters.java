package net.pkhapps.idispatch.gis.postgis.bindings;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.LineString;
import org.locationtech.jts.geom.Point;

import java.util.stream.Stream;

/**
 * TODO Document me!
 */
public final class PostgisConverters {

    private static final GeometryFactory GEOMETRY_FACTORY = new GeometryFactory();

    private PostgisConverters() {
    }

    public static @NotNull org.postgis.Point toPostgis(@NotNull Point point) {
        var pg = new org.postgis.Point();
        pg.setSrid(point.getSRID());
        var coordinate = point.getCoordinate();
        pg.setX(coordinate.getX());
        pg.setY(coordinate.getY());
        if (Double.isNaN(coordinate.getZ())) {
            pg.dimension = 2;
        } else {
            pg.dimension = 3;
            pg.setZ(coordinate.getZ());
        }
        return pg;
    }

    @Contract("null -> null")
    public static Point fromPostgis(org.postgis.Point point) {
        if (point == null) {
            return null;
        }
        var coordinate = new Coordinate(point.getX(), point.getY());
        if (point.dimension == 3) {
            coordinate.setZ(point.getZ());
        }
        var jts = GEOMETRY_FACTORY.createPoint(coordinate);
        jts.setSRID(point.getSrid());
        return jts;
    }

    @Contract("null -> null")
    public static LineString fromPostgis(org.postgis.LineString lineString) {
        if (lineString == null) {
            return null;
        }
        var coordinates = Stream.of(lineString.getPoints())
                .map(point -> new Coordinate(point.getX(), point.getY()))
                .toArray(Coordinate[]::new);
        var jts = GEOMETRY_FACTORY.createLineString(coordinates);
        jts.setSRID(lineString.getSrid());
        return jts;
    }
}
