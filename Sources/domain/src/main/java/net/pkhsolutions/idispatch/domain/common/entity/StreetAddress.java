/*
 * TODO Decide which license to use!
 */
package net.pkhsolutions.idispatch.domain.common.entity;

import javax.persistence.Embeddable;
import javax.persistence.Embedded;
import javax.persistence.ManyToOne;
import javax.validation.Valid;
import net.pkhsolutions.idispatch.domain.AbstractValueObject;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

/**
 * Value object representing a street address in a municipality.
 *
 * @author Petter Holmström
 * @since 1.0
 */
@Embeddable
public class StreetAddress extends AbstractValueObject<StreetAddress> {

    @Embedded
    @Valid
    private GeoLocation location = GeoLocation.NULL();
    private String street;
    private String streetNumber;
    @ManyToOne
    private Municipality municipality;
    private boolean accurate = false;

    public static class Builder extends AbstractBuilder<StreetAddress> {

        public Builder() {
            super();
        }

        private Builder(StreetAddress address) {
            super(address);
        }

        public Builder atLocation(GeoLocation location) {
            if (location == null) {
                location = GeoLocation.NULL();
            }
            getValueObject().location = location;
            return this;
        }

        public Builder inStreet(String street) {
            getValueObject().street = street;
            return this;
        }

        public Builder withStreetNumber(String streetNumber) {
            getValueObject().streetNumber = streetNumber;
            return this;
        }

        public Builder inMunicipality(Municipality municipality) {
            getValueObject().municipality = municipality;
            return this;
        }

        public Builder accurateAddress() {
            getValueObject().accurate = true;
            return this;
        }

        public Builder inaccurateAddress() {
            getValueObject().accurate = false;
            return this;
        }
    }

    protected StreetAddress() {
    }

    public static StreetAddress NULL() {
        StreetAddress n = new StreetAddress();
        n.setToNull();
        return n;
    }

    public GeoLocation getLocation() {
        return location;
    }

    public String getStreet() {
        return street;
    }

    public String getStreetNumber() {
        return streetNumber;
    }

    public Municipality getMunicipality() {
        return municipality;
    }

    public boolean isAccurate() {
        return accurate;
    }

    @Override
    protected void internalSetValueToNull() {
        location = GeoLocation.NULL();
        street = null;
        streetNumber = null;
        municipality = null;
        accurate = false;
    }

    @Override
    protected int internalCalculateHashCode() {
        return new HashCodeBuilder()
                .append(location)
                .append(street)
                .append(streetNumber)
                .append(municipality)
                .append(accurate)
                .toHashCode();
    }

    @Override
    protected void internalCopy(StreetAddress destination) {
        destination.location = location.copy();
        destination.street = street;
        destination.streetNumber = streetNumber;
        destination.accurate = accurate;
        destination.municipality = municipality;
    }

    @Override
    public boolean hasSameValue(StreetAddress value) {
        return new EqualsBuilder()
                .append(location, value.location)
                .append(street, value.street)
                .append(streetNumber, value.streetNumber)
                .append(municipality, value.municipality)
                .append(accurate, value.accurate)
                .isEquals();
    }

    @Override
    public Builder derive() {
        return new Builder(this);
    }
}
