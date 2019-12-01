package net.pkhapps.idispatch.gis.api;

import org.geotools.geometry.jts.JTS;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.locationtech.jts.geom.Geometry;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.operation.MathTransform;
import org.opengis.referencing.operation.TransformException;

/**
 * Utility class for converting between WGS84 and ETRS89 / TM35FIN.
 */
@SuppressWarnings({"WeakerAccess", "SpellCheckingInspection"})
public final class CRS {

    /**
     * The CRS for ETRS89 / TM35FIN
     */
    public static final CoordinateReferenceSystem ETRS89_TM35FIN;
    /**
     * The CRS for WGS84
     */
    public static final CoordinateReferenceSystem WGS84;
    /**
     * The SRID for ETRS89 / TM35FIN
     */
    public static final int ETRS89_TM35FIN_SRID = 3067;
    /**
     * The SRID for WGS84
     */
    public static final int WGS84_SRID = 4326;

    private static final MathTransform WGS84_TO_TM35FIN;
    private static final MathTransform TM35FIN_TO_WGS84;

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
            TM35FIN_TO_WGS84 = org.geotools.referencing.CRS.findMathTransform(ETRS89_TM35FIN, WGS84);
        } catch (Exception ex) {
            throw new IllegalStateException("Could not set up coordinate reference systems", ex);
        }
    }

    private CRS() {
    }

    /**
     * Converts the given WGS84 geometry to ETRS89 / TM35FIN.
     *
     * @param geometry the geometry to convert or {@code null}
     * @param <G>      the type of the geometry
     * @return the converted geometry or {@code null} if the input argument was {@code null}
     */
    @Contract("null -> null")
    public static <G extends Geometry> G wgs84ToEtrs89Tm35Fin(@Nullable G geometry) {
        return convert(geometry, WGS84_TO_TM35FIN, ETRS89_TM35FIN_SRID);
    }

    /**
     * TODO Document me
     *
     * @param geometry
     * @param <G>
     * @return
     */
    @Contract("null -> null")
    public static <G extends Geometry> G etrs89Tm35FinToWgs84(@Nullable G geometry) {
        return convert(geometry, TM35FIN_TO_WGS84, WGS84_SRID);
    }

    @SuppressWarnings("unchecked")
    @Contract("null,_,_ -> null")
    private static <G extends Geometry> G convert(@Nullable G geometry, @NotNull MathTransform transform,
                                                  @NotNull int destinationSrid) {
        if (geometry == null) {
            return null;
        }
        try {
            var transformed = (G) JTS.transform(geometry, transform);
            transformed.setSRID(destinationSrid);
            return transformed;
        } catch (TransformException ex) {
            throw new RuntimeException("Could not convert geometry", ex);
        }
    }
}
