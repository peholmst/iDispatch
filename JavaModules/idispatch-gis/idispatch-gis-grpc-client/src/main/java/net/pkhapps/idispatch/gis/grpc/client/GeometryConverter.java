package net.pkhapps.idispatch.gis.grpc.client;

import net.pkhapps.idispatch.gis.grpc.proto.GIS;
import org.jetbrains.annotations.NotNull;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;

/**
 * TODO Document me
 */
final class GeometryConverter {

    private static final GeometryFactory GEOMETRY_FACTORY = new GeometryFactory();

    private GeometryConverter() {
    }

    static @NotNull Coordinate fromMessage(@NotNull GIS.Coordinate coordinate) {
        return new Coordinate(coordinate.getX(), coordinate.getY());
    }

    static @NotNull Point fromMessage(@NotNull GIS.Point point) {
        var p = GEOMETRY_FACTORY.createPoint(fromMessage(point.getCoordinate()));
        p.setSRID(point.getSrid());
        return p;
    }
}
