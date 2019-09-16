package net.pkhapps.idispatch.core.domain.geo;

import net.pkhapps.idispatch.core.domain.i18n.MultilingualString;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

/**
 * Value object representing an intersection of two roads.
 */
public class Intersection extends RoadLocation {

    private final MultilingualString intersectingRoadName;

    public Intersection(@NotNull Position coordinates,
                        @Nullable MunicipalityId municipality,
                        @Nullable String additionalDetails,
                        @NotNull MultilingualString roadName,
                        @NotNull MultilingualString intersectingRoadName) {
        super(coordinates, municipality, additionalDetails, roadName);
        this.intersectingRoadName = intersectingRoadName;
    }

    /**
     * Returns the name of the intersecting road.
     */
    public final @NotNull MultilingualString intersectingRoadName() {
        return intersectingRoadName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Intersection that = (Intersection) o;
        return intersectingRoadName.equals(that.intersectingRoadName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), intersectingRoadName);
    }
}
