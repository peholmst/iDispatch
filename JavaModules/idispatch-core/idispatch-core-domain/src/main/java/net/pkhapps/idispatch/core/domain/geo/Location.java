package net.pkhapps.idispatch.core.domain.geo;

import net.pkhapps.idispatch.core.domain.common.ValueObject;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.Optional;

import static java.util.Objects.requireNonNull;

/**
 * Base class for a geographical location.
 */
public abstract class Location implements ValueObject {

    private final Position coordinates;
    private final MunicipalityId municipality;
    private final String additionalDetails;

    protected Location(@NotNull Position coordinates,
                       @Nullable MunicipalityId municipality,
                       @Nullable String additionalDetails) {
        this.coordinates = requireNonNull(coordinates);
        this.municipality = municipality;
        this.additionalDetails = additionalDetails;
    }

    /**
     * The coordinates of the location.
     */
    public final @NotNull Position coordinates() {
        return coordinates;
    }

    /**
     * The municipality of the location, if known or applicable.
     */
    public final @NotNull Optional<MunicipalityId> municipality() {
        return Optional.ofNullable(municipality);
    }

    /**
     * Any additional details about the location.
     */
    public final @NotNull Optional<String> additionalDetails() {
        return Optional.ofNullable(additionalDetails);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Location location = (Location) o;
        return coordinates.equals(location.coordinates)
                && Objects.equals(municipality, location.municipality)
                && Objects.equals(additionalDetails, location.additionalDetails);
    }

    @Override
    public int hashCode() {
        return Objects.hash(coordinates, municipality, additionalDetails);
    }
}
