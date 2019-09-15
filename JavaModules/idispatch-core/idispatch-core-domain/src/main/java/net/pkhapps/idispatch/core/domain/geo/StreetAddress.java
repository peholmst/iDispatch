package net.pkhapps.idispatch.core.domain.geo;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.Optional;

/**
 * Value object representing an exact street address.
 */
public class StreetAddress extends RoadLocation {

    private final String number;
    private final String apartment;

    public StreetAddress(@NotNull Position coordinates,
                         @Nullable MunicipalityId municipality,
                         @Nullable String additionalDetails,
                         @NotNull LocationName roadName,
                         @Nullable String number,
                         @Nullable String apartment) {
        super(coordinates, municipality, additionalDetails, roadName);
        this.number = number;
        this.apartment = apartment;
    }

    /**
     * The number of the building, if known or applicable.
     */
    public @NotNull Optional<String> number() {
        return Optional.ofNullable(number);
    }

    /**
     * The number of the apartment, if known or applicable.
     */
    public @NotNull Optional<String> apartment() {
        return Optional.ofNullable(apartment);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        StreetAddress that = (StreetAddress) o;
        return Objects.equals(number, that.number) &&
                Objects.equals(apartment, that.apartment);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), number, apartment);
    }
}
