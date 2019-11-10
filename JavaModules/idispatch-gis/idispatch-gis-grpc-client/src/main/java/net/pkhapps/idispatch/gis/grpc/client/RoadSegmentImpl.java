package net.pkhapps.idispatch.gis.grpc.client;

import net.pkhapps.idispatch.gis.api.lookup.AddressNumberRange;
import net.pkhapps.idispatch.gis.api.lookup.NamedFeature;
import net.pkhapps.idispatch.gis.api.lookup.RoadSegment;
import net.pkhapps.idispatch.gis.api.lookup.code.Elevation;
import net.pkhapps.idispatch.gis.api.lookup.code.RoadClass;
import net.pkhapps.idispatch.gis.api.lookup.code.RoadDirection;
import net.pkhapps.idispatch.gis.api.lookup.code.RoadSurface;
import net.pkhapps.idispatch.gis.grpc.proto.GIS;
import net.pkhapps.idispatch.gis.grpc.util.ConversionUtil;
import org.jetbrains.annotations.NotNull;
import org.locationtech.jts.geom.LineString;

import java.util.Locale;
import java.util.Optional;

/**
 * TODO Document me
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
    private final LineString location;
    private final NamedFeature name;

    RoadSegmentImpl(@NotNull GIS.LocationFeature message) {
        super(message);
        var rs = message.getRoadSegment();
        roadClass = RoadClass.valueOf(rs.getRoadClass());
        elevation = Elevation.valueOf(rs.getElevation());
        surface = RoadSurface.valueOf(rs.getSurface());
        direction = RoadDirection.valueOf(rs.getDirection());
        roadNumber = rs.hasRoadNumber() ? rs.getRoadNumber().getValue() : null;
        roadPartNumber = rs.hasRoadPartNumber() ? rs.getRoadPartNumber().getValue() : null;
        addressNumbersLeft = rs.hasAddressNumbersLeft() ? ConversionUtil.fromMessage(rs.getAddressNumbersLeft()) : null;
        addressNumbersRight = rs.hasAddressNumbersRight() ? ConversionUtil.fromMessage(rs.getAddressNumbersRight()) : null;
        location = ConversionUtil.fromMessage(rs.getLocation());
        name = ConversionUtil.fromMessage(rs.getName());
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

    @NotNull
    @Override
    public LineString getLocation() {
        return location;
    }

    @Override
    public @NotNull Optional<String> getName(@NotNull Locale locale) {
        return name.getName(locale);
    }
}
