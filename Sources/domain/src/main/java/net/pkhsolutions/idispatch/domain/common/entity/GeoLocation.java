/*
 * TODO Decide which license to use!
 */
package net.pkhsolutions.idispatch.domain.common.entity;

import java.math.BigDecimal;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import net.pkhsolutions.idispatch.domain.AbstractValueObject;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

/**
 * Value object representing a geographic location specified by its latitude and
 * longitude coordinates.
 *
 * @author Petter Holmström
 * @since 1.0
 */
@Embeddable
public class GeoLocation extends AbstractValueObject<GeoLocation> {

    @Column(precision = 10, scale = 7)
    private BigDecimal longitude;
    @Column(precision = 9, scale = 7)
    private BigDecimal latitude;

    public static class Builder extends AbstractBuilder<GeoLocation> {

        public Builder() {
            super();
        }

        private Builder(GeoLocation geoLocation) {
            super(geoLocation);
        }

        public Builder withLatitude(BigDecimal latitude) {
            getValueObject().latitude = latitude;
            return this;
        }

        public Builder withLongitude(BigDecimal longitude) {
            getValueObject().longitude = longitude;
            return this;
        }
    }

    protected GeoLocation() {
    }

    public static GeoLocation NULL() {
        GeoLocation n = new GeoLocation();
        n.setToNull();
        return n;
    }

    public BigDecimal getLatitude() {
        return latitude;
    }

    public BigDecimal getLongitude() {
        return longitude;
    }

    @Override
    public Builder derive() {
        return new Builder(copy());
    }

    @Override
    protected void internalSetValueToNull() {
        latitude = null;
        longitude = null;
    }

    @Override
    protected void internalCopy(GeoLocation destination) {
        destination.latitude = latitude;
        destination.longitude = longitude;
    }

    @Override
    protected int internalCalculateHashCode() {
        return new HashCodeBuilder()
                .append(latitude)
                .append(longitude)
                .toHashCode();
    }

    @Override
    public boolean hasSameValue(GeoLocation value) {
        return new EqualsBuilder()
                .append(latitude, value.latitude)
                .append(longitude, value.longitude)
                .isEquals();
    }
}
