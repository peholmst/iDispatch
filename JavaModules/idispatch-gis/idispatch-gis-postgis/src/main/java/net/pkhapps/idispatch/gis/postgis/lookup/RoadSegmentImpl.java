package net.pkhapps.idispatch.gis.postgis.lookup;

import net.pkhapps.idispatch.gis.api.lookup.AddressNumberRange;
import net.pkhapps.idispatch.gis.api.lookup.RoadSegment;
import net.pkhapps.idispatch.gis.api.lookup.code.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.locationtech.jts.geom.LineString;

import java.time.LocalDate;
import java.util.Optional;

import static java.util.Objects.requireNonNull;

/**
 * TOOD Document me
 */
class RoadSegmentImpl extends LocationFeatureImpl<LineString> implements RoadSegment {

    private final RoadClass roadClass;
    private final Elevation elevation;
    private final RoadSurface surface;
    private final RoadDirection direction;
    private final Long roadNumber;
    private final Long roadPartNumber;
    private final Integer addressNumberLeftMax;
    private final Integer addressNumberLeftMin;
    private final Integer addressNumberRightMax;
    private final Integer addressNumberRightMin;

    RoadSegmentImpl(@NotNull LocationAccuracy locationAccuracy,
                    @Nullable LocalDate validFrom,
                    @Nullable LocalDate validTo,
                    @Nullable LineString lineString,
                    @Nullable MunicipalityCode municipality,
                    @Nullable String nameSv,
                    @Nullable String nameFi,
                    @Nullable String nameSe,
                    @Nullable String nameSmn,
                    @Nullable String nameSms,
                    @NotNull RoadClass roadClass,
                    @NotNull Elevation elevation,
                    @NotNull RoadSurface surface,
                    @NotNull RoadDirection direction,
                    @Nullable Long roadNumber,
                    @Nullable Long roadPartNumber,
                    @Nullable Integer addressNumberLeftMax,
                    @Nullable Integer addressNumberLeftMin,
                    @Nullable Integer addressNumberRightMax,
                    @Nullable Integer addressNumberRightMin) {
        super(locationAccuracy, validFrom, validTo, lineString, municipality, nameSv, nameFi, nameSe, nameSmn, nameSms);
        this.roadClass = requireNonNull(roadClass);
        this.elevation = requireNonNull(elevation);
        this.surface = requireNonNull(surface);
        this.direction = requireNonNull(direction);
        this.roadNumber = roadNumber;
        this.roadPartNumber = roadPartNumber;
        this.addressNumberLeftMax = addressNumberLeftMax;
        this.addressNumberLeftMin = addressNumberLeftMin;
        this.addressNumberRightMax = addressNumberRightMax;
        this.addressNumberRightMin = addressNumberRightMin;
    }

    @Override
    public @NotNull RoadClass getRoadClass() {
        return roadClass;
    }

    @Override
    public @NotNull Elevation getElevation() {
        return elevation;
    }

    @Override
    public @NotNull RoadSurface getSurface() {
        return surface;
    }

    @Override
    public @NotNull RoadDirection getDirection() {
        return direction;
    }

    @Override
    public @NotNull Optional<Long> getRoadNumber() {
        return Optional.ofNullable(roadNumber);
    }

    @Override
    public @NotNull Optional<Long> getRoadPartNumber() {
        return Optional.ofNullable(roadPartNumber);
    }

    @Override
    public @NotNull Optional<AddressNumberRange> getAddressNumbersLeft() {
        if (addressNumberLeftMax == null && addressNumberLeftMin == null) {
            return Optional.empty();
        } else {
            return Optional.of(new AddressNumberRangeImpl(addressNumberLeftMin, addressNumberLeftMax));
        }
    }

    @Override
    public @NotNull Optional<AddressNumberRange> getAddressNumbersRight() {
        if (addressNumberRightMax == null && addressNumberRightMin == null) {
            return Optional.empty();
        } else {
            return Optional.of(new AddressNumberRangeImpl(addressNumberRightMin, addressNumberRightMax));
        }
    }
}
