package net.pkhapps.idispatch.gis.api;

import org.jetbrains.annotations.NotNull;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;

/**
 * TODO Document me
 */
public final class GeometryUtil {

    private static final GeometryFactory GEOMETRY_FACTORY = new GeometryFactory();

    private GeometryUtil() {
    }

    /**
     * @param longitude
     * @param latitude
     * @return
     */
    public static @NotNull Point createWgs84Point(double longitude, double latitude) {
        var point = GEOMETRY_FACTORY.createPoint(new Coordinate(longitude, latitude));
        point.setSRID(CRS.WGS84_SRID);
        return point;
    }

    /**
     * @param easting
     * @param northing
     * @return
     */
    public static @NotNull Point createEtrs89Tm35FinPoint(double easting, double northing) {
        var point = GEOMETRY_FACTORY.createPoint(new Coordinate(easting, northing));
        point.setSRID(CRS.ETRS89_TM35FIN_SRID);
        return point;
    }
}
