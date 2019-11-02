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
 * Implementation of {@link RoadSegment}.
 */
class RoadSegmentImpl extends LocationFeatureImpl<LineString> implements RoadSegment {

    private final RoadClass roadClass;
    private final Elevation elevation;
    private final RoadSurface surface;
    private final RoadDirection direction;
    private final Long roadNumber;
    private final Long roadPartNumber;
    private final AddressNumberRange addressNumbersLeft;
    private final AddressNumberRange addressNumbersRight;

    RoadSegmentImpl(@NotNull LocationAccuracy locationAccuracy,
                    @Nullable LocalDate validFrom,
                    @Nullable LocalDate validTo,
                    @NotNull LineString lineString,
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
        if (addressNumberLeftMax != null && addressNumberLeftMin != null) {
            addressNumbersLeft = new AddressNumberRangeImpl(addressNumberLeftMin, addressNumberLeftMax);
        } else {
            addressNumbersLeft = null;
        }
        if (addressNumberRightMax != null && addressNumberRightMin != null) {
            addressNumbersRight = new AddressNumberRangeImpl(addressNumberRightMin, addressNumberRightMax);
        } else {
            addressNumbersRight = null;
        }
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
        return Optional.ofNullable(addressNumbersLeft);
    }

    @Override
    public @NotNull Optional<AddressNumberRange> getAddressNumbersRight() {
        return Optional.ofNullable(addressNumbersRight);
    }

    @Override
    void buildToString(@NotNull StringBuilder sb) {
        super.buildToString(sb);
        sb.append(", ");
        sb.append("roadClass=").append(roadClass).append(", ");
        sb.append("elevation=").append(elevation).append(", ");
        sb.append("surface=").append(surface).append(", ");
        sb.append("direction=").append(direction).append(", ");
        sb.append("roadNumber=").append(roadNumber).append(", ");
        sb.append("roadPartNumber=").append(roadPartNumber).append(", ");
        sb.append("addressNumbersLeft=").append(addressNumbersLeft).append(", ");
        sb.append("addressNumbersRight=").append(addressNumbersRight);
    }
}
