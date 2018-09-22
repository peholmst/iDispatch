package net.pkhapps.idispatch.client.v3.geo;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.Immutable;
import java.io.Serializable;
import java.util.Objects;

/**
 * Value object that represents an absolute geographic location.
 */
@Immutable
@SuppressWarnings("WeakerAccess")
public class GeographicLocation implements Serializable {

    private CoordinateReferenceSystem crs;
    private double lat;
    private double lng;

    public GeographicLocation(@Nonnull CoordinateReferenceSystem crs, double lat, double lng) {
        this.crs = Objects.requireNonNull(crs, "crs must not be null");
        this.lat = lat;
        this.lng = lng;
    }

    public double latitude() {
        return lat;
    }

    public double longitude() {
        return lng;
    }

    public double x() {
        return longitude();
    }

    public double y() {
        return latitude();
    }

    @Nonnull
    public CoordinateReferenceSystem crs() {
        return crs;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GeographicLocation that = (GeographicLocation) o;
        return Objects.equals(that.crs, crs) &&
                Double.compare(that.lat, lat) == 0 &&
                Double.compare(that.lng, lng) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(crs, lat, lng);
    }

    @Override
    public String toString() {
        return String.format("%s[crs=%s, lat=%f, lng=%f]", getClass().getSimpleName(), crs, lat, lng);
    }
}
