package net.pkhapps.idispatch.gis.mongodb.importer;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.locationtech.jts.geom.Coordinate;

import java.util.ArrayList;
import java.util.List;

import static net.pkhapps.idispatch.gis.mongodb.importer.CoordinateReferenceSystems.toWGS84;

/**
 * TODO Document me
 */
final class GeoJsonConverters {

    private GeoJsonConverters() {
    }

    @Contract("null -> null")
    static Point toPoint(org.locationtech.jts.geom.Point point) {
        if (point == null) {
            return null;
        }
        var wgs84 = toWGS84(point).getCoordinate();
        return new Point(new Position(wgs84.getOrdinate(0), wgs84.getOrdinate(1)));
    }

    @Contract("null -> null")
    static MultiPolygon toMultiPolygon(org.locationtech.jts.geom.MultiPolygon multiPolygon) {
        var coordinates = new ArrayList<PolygonCoordinates>();
        for (var n = 0; n < multiPolygon.getNumGeometries(); ++n) {
            coordinates.add(toPolygon((org.locationtech.jts.geom.Polygon) multiPolygon.getGeometryN(n)).getCoordinates());
        }
        return new MultiPolygon(coordinates);
    }

    @Contract("null -> null")
    @SuppressWarnings("unchecked")
    static Polygon toPolygon(org.locationtech.jts.geom.Polygon polygon) {
        if (polygon == null) {
            return null;
        }
        var wgs84 = toWGS84(polygon);
        var exterior = toPositions(wgs84.getExteriorRing());
        var interiors = new List[wgs84.getNumInteriorRing()];
        for (var n = 0; n < wgs84.getNumInteriorRing(); ++n) {
            interiors[n] = toPositions(wgs84.getInteriorRingN(n));
        }
        return new Polygon(exterior, interiors);
    }

    @NotNull
    private static List<Position> toPositions(@NotNull org.locationtech.jts.geom.LineString ring) {
        var positions = new ArrayList<Position>();
        for (Coordinate coordinate : ring.getCoordinates()) {
            positions.add(new Position(coordinate.getOrdinate(0), coordinate.getOrdinate(1)));
        }
        return positions;
    }
}
