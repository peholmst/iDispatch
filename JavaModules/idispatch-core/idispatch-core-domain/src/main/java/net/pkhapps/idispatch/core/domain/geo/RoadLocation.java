package net.pkhapps.idispatch.core.domain.geo;

import org.geotools.geometry.DirectPosition2D;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

import static java.util.Objects.requireNonNull;

/**
 * Value object representing a location that is on a particular road, but not at a numbered spot like an address.
 * For example: "Road X, about Y km from city Z".
 */
public class RoadLocation extends Location {

    private final LocationName roadName;

    public RoadLocation(@NotNull DirectPosition2D coordinates,
                        @Nullable MunicipalityId municipality,
                        @Nullable String additionalDetails,
                        @NotNull LocationName roadName) {
        super(coordinates, municipality, additionalDetails);
        this.roadName = requireNonNull(roadName);
    }

    /**
     * Returns the name of the road that the location is on.
     */
    public final @NotNull LocationName roadName() {
        return roadName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        RoadLocation that = (RoadLocation) o;
        return roadName.equals(that.roadName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), roadName);
    }
}
