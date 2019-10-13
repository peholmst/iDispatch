package net.pkhapps.idispatch.gis.api;

import org.geotools.geometry.jts.JTS;
import org.jetbrains.annotations.Contract;
import org.locationtech.jts.geom.Geometry;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.operation.MathTransform;
import org.opengis.referencing.operation.TransformException;

/**
 * TODO Document me
 */
public final class CRS {

    public static final CoordinateReferenceSystem ETRS89_TM35FIN;
    public static final CoordinateReferenceSystem WGS84;

    public static final int ETRS89_TM35FIN_SRID = 3067;
    public static final int WGS84_SRID = 4326;
    private static final MathTransform WGS84_TO_TM35FIN;

    static {
        try {
            ETRS89_TM35FIN = org.geotools.referencing.CRS.parseWKT("PROJCS[\"ETRS89 / TM35FIN(E,N)\",\n" +
                    "    GEOGCS[\"ETRS89\",\n" +
                    "        DATUM[\"European_Terrestrial_Reference_System_1989\",\n" +
                    "            SPHEROID[\"GRS 1980\",6378137,298.257222101,\n" +
                    "                AUTHORITY[\"EPSG\",\"7019\"]],\n" +
                    "            TOWGS84[0,0,0,0,0,0,0],\n" +
                    "            AUTHORITY[\"EPSG\",\"6258\"]],\n" +
                    "        PRIMEM[\"Greenwich\",0,\n" +
                    "            AUTHORITY[\"EPSG\",\"8901\"]],\n" +
                    "        UNIT[\"degree\",0.0174532925199433,\n" +
                    "            AUTHORITY[\"EPSG\",\"9122\"]],\n" +
                    "        AUTHORITY[\"EPSG\",\"4258\"]],\n" +
                    "    PROJECTION[\"Transverse_Mercator\"],\n" +
                    "    PARAMETER[\"latitude_of_origin\",0],\n" +
                    "    PARAMETER[\"central_meridian\",27],\n" +
                    "    PARAMETER[\"scale_factor\",0.9996],\n" +
                    "    PARAMETER[\"false_easting\",500000],\n" +
                    "    PARAMETER[\"false_northing\",0],\n" +
                    "    UNIT[\"metre\",1,\n" +
                    "        AUTHORITY[\"EPSG\",\"9001\"]],\n" +
                    "    AXIS[\"Easting\",EAST],\n" +
                    "    AXIS[\"Northing\",NORTH],\n" +
                    "    AUTHORITY[\"EPSG\",\"3067\"]]");

            WGS84 = org.geotools.referencing.CRS.parseWKT("GEOGCS[\"WGS 84\",\n" +
                    "    DATUM[\"WGS_1984\",\n" +
                    "        SPHEROID[\"WGS 84\",6378137,298.257223563,\n" +
                    "            AUTHORITY[\"EPSG\",\"7030\"]],\n" +
                    "        AUTHORITY[\"EPSG\",\"6326\"]],\n" +
                    "    PRIMEM[\"Greenwich\",0,\n" +
                    "        AUTHORITY[\"EPSG\",\"8901\"]],\n" +
                    "    UNIT[\"degree\",0.0174532925199433,\n" +
                    "        AUTHORITY[\"EPSG\",\"9122\"]],\n" +
                    "    AUTHORITY[\"EPSG\",\"4326\"]]");

            WGS84_TO_TM35FIN = org.geotools.referencing.CRS.findMathTransform(WGS84, ETRS89_TM35FIN);
        } catch (Exception ex) {
            throw new IllegalStateException("Could not set up coordinate reference systems", ex);
        }
    }

    private CRS() {
    }

    @SuppressWarnings("unchecked")
    @Contract("null -> null")
    public static <G extends Geometry> G wgs84ToEtrs89Tm35Fin(G geometry) {
        if (geometry == null) {
            return null;
        }
        try {
            return (G) JTS.transform(geometry, WGS84_TO_TM35FIN);
        } catch (TransformException ex) {
            throw new RuntimeException("Could not convert geometry", ex);
        }
    }
}
