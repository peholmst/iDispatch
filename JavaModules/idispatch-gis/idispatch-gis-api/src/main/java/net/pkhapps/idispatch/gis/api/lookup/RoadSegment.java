package net.pkhapps.idispatch.gis.api.lookup;

import net.pkhapps.idispatch.gis.api.lookup.code.Elevation;
import net.pkhapps.idispatch.gis.api.lookup.code.RoadClass;
import net.pkhapps.idispatch.gis.api.lookup.code.RoadDirection;
import net.pkhapps.idispatch.gis.api.lookup.code.RoadSurface;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

/**
 * TODO Document me
 */
public interface RoadSegment extends LineStringFeature, NamedFeature {

    @NotNull RoadClass getRoadClass();

    @NotNull Elevation getElevation();

    @NotNull RoadSurface getSurface();

    @NotNull RoadDirection getDirection();

    @NotNull Optional<Integer> getRoadNumber();

    @NotNull Optional<Integer> getRoadPartNumber();

    @NotNull Optional<AddressNumberRange> getAddressNumbersLeft();

    @NotNull Optional<AddressNumberRange> getAddressNumbersRight();
}
