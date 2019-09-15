package net.pkhapps.idispatch.core.domain.geo;

import net.pkhapps.idispatch.core.domain.common.ValueObject;
import org.geotools.geometry.DirectPosition2D;
import org.geotools.referencing.CRS;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

/**
 * TODO Document me!
 */
public class Position implements ValueObject {

    private final double easting;
    private final double northing;

    private Position(double easting, double northing) {
        this.easting = easting;
        this.northing = northing;
    }

    public double getEasting() {
        return easting;
    }

    public double getNorthing() {
        return northing;
    }

    public static @NotNull Position createFromTm35Fin(double easting, double northing) {
        return new Position(easting, northing);
    }

    public static @NotNull Position createFromWgs84(double latitude, double longitude) {
        // TODO Implement me!
        throw new UnsupportedOperationException("Not implemented yet");
    }

    public static @NotNull Position createFromDirectPosition(@NotNull DirectPosition2D directPosition) {
        // TODO Implement me!
        throw new UnsupportedOperationException("Not implemented yet");
    }

    public @NotNull DirectPosition2D toWgs84() {
        try {
            // TODO Do we need to cache this transform?
            var mathTransform = CRS.findMathTransform(CoordinateReferenceSystems.TM35FIN,
                    CoordinateReferenceSystems.WGS84);
            return (DirectPosition2D) mathTransform.transform(toTm35Fin(),
                    new DirectPosition2D(CoordinateReferenceSystems.WGS84));
        } catch (Exception ex) {
            throw new IllegalStateException("Could not transform coordinates to WGS84", ex);
        }
    }

    public @NotNull DirectPosition2D toTm35Fin() {
        return new DirectPosition2D(CoordinateReferenceSystems.TM35FIN, easting, northing);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Position position = (Position) o;
        return Double.compare(position.easting, easting) == 0 &&
                Double.compare(position.northing, northing) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(easting, northing);
    }
}
