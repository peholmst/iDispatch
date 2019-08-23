package net.pkhapps.idispatch.core.domain.geo;

import net.pkhapps.idispatch.core.domain.common.ValueObject;
import org.geotools.geometry.DirectPosition2D;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.Optional;

import static java.util.Objects.requireNonNull;

/**
 * Base class for a geographical location.
 */
public abstract class Location implements ValueObject {

    private final DirectPosition2D coordinates;
    private final MunicipalityId municipality;
    private final String additionalDetails;

    protected Location(@NotNull DirectPosition2D coordinates,
                       @Nullable MunicipalityId municipality,
                       @Nullable String additionalDetails) {
        this.coordinates = requireNonNull(coordinates).clone();
        this.municipality = municipality;
        this.additionalDetails = additionalDetails;
    }

    /**
     * Returns the coordinates of the location.
     */
    public final @NotNull DirectPosition2D coordinates() {
        return coordinates.clone();
    }

    /**
     * Returns the municipality of the location, if known or applicable.
     */
    public final @NotNull Optional<MunicipalityId> municipality() {
        return Optional.ofNullable(municipality);
    }

    /**
     * Returns any additional details about the location.
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
