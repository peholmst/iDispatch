package net.pkhapps.idispatch.gis.api.lookup;

import net.pkhapps.idispatch.gis.api.lookup.code.Elevation;
import net.pkhapps.idispatch.gis.api.lookup.code.RoadClass;
import net.pkhapps.idispatch.gis.api.lookup.code.RoadDirection;
import net.pkhapps.idispatch.gis.api.lookup.code.RoadSurface;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

/**
 * Road segments are parts of a road. They can have name, numbers, address numbers or be more or less completely
 * anonymous.
 */
public interface RoadSegment extends LineStringFeature, NamedFeature {

    /**
     * The class of this road segment
     */
    @NotNull RoadClass getRoadClass();

    /**
     * The (relative) elevation of this road segment
     */
    @NotNull Elevation getElevation();

    /**
     * The surface of this road segment
     */
    @NotNull RoadSurface getSurface();

    /**
     * The direction in which you drive on this road segment
     */
    @NotNull RoadDirection getDirection();

    /**
     * The number of the road if applicable. Please note that this is not the same as an address number.
     */
    @NotNull Optional<Long> getRoadNumber();

    /**
     * The number of the road part if applicable. Please note that this is not the same as an address number.
     */
    @NotNull Optional<Long> getRoadPartNumber();

    /**
     * The range of address numbers on the left side of this road segment if applicable.
     */
    @NotNull Optional<AddressNumberRange> getAddressNumbersLeft();

    /**
     * The range of address numbers on the right side of this road segment if applicable.
     */
    @NotNull Optional<AddressNumberRange> getAddressNumbersRight();

    /**
     * Checks whether this road segment intersects the given road segment.
     *
     * @param otherRoad the potentially intersecting road segment
     * @return true if the two road segments intersect, false it they don't
     */
    default boolean intersects(@NotNull RoadSegment otherRoad) {
        return getLocation().intersects(otherRoad.getLocation());
    }
}
