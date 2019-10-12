package net.pkhapps.idispatch.gis.mongodb.importer;

import org.geotools.geometry.GeometryBuilder;
import org.geotools.geometry.jts.JTS;
import org.geotools.referencing.CRS;
import org.jetbrains.annotations.NotNull;
import org.locationtech.jts.geom.Geometry;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.operation.MathTransform;
import org.opengis.referencing.operation.TransformException;

/**
 * TODO Document me
 */
final class CoordinateReferenceSystems {

    private static final String CRS_TM35FIN = "urn:x-ogc:def:crs:EPSG:3067";
    private static final String CRS_WGS84 = "urn:x-ogc:def:crs:EPSG:4326";
    static final CoordinateReferenceSystem TM35FIN;
    static final CoordinateReferenceSystem WGS84;
    static final GeometryBuilder TM35FIN_BUILDER;
    static final GeometryBuilder WGS84_BUILDER;
    private static final MathTransform TRANSFORM;

    static {
        try {
            TM35FIN = CRS.decode(CRS_TM35FIN);
            WGS84 = CRS.decode(CRS_WGS84);
            TRANSFORM = CRS.findMathTransform(TM35FIN, WGS84);
            TM35FIN_BUILDER = new GeometryBuilder(TM35FIN);
            WGS84_BUILDER = new GeometryBuilder(WGS84);
        } catch (Exception ex) {
            throw new IllegalStateException("Could not setup CRSes and transforms between them", ex);
        }
    }

    private CoordinateReferenceSystems() {
    }

    // TODO these implementation assumes the source is TM35FIN. Add a check for this. If the source is already WGS84, return it immediately.

    @SuppressWarnings("unchecked")
    public static <T extends Geometry> @NotNull T toWGS84(@NotNull T source) {
        try {
            return (T) JTS.transform(source, TRANSFORM);
        } catch (TransformException ex) {
            throw new IllegalStateException("Cannot transform TM35FIN coordinates to WGS84", ex);
        }
    }
}
