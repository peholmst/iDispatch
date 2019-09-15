package net.pkhapps.idispatch.core.domain.geo;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

/**
 * Value object representing an intersection of two roads.
 */
public class Intersection extends RoadLocation {

    private final LocationName intersectingRoad;

    public Intersection(@NotNull Position coordinates,
                        @Nullable MunicipalityId municipality,
                        @Nullable String additionalDetails,
                        @NotNull LocationName roadName,
                        @NotNull LocationName intersectingRoad) {
        super(coordinates, municipality, additionalDetails, roadName);
        this.intersectingRoad = intersectingRoad;
    }

    /**
     * Returns the name of the intersecting road.
     */
    public final @NotNull LocationName intersectingRoad() {
        return intersectingRoad;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Intersection that = (Intersection) o;
        return intersectingRoad.equals(that.intersectingRoad);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), intersectingRoad);
    }
}
