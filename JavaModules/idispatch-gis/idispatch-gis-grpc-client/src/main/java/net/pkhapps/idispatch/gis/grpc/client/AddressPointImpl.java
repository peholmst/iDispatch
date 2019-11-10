package net.pkhapps.idispatch.gis.grpc.client;

import net.pkhapps.idispatch.gis.api.lookup.AddressPoint;
import net.pkhapps.idispatch.gis.api.lookup.NamedFeature;
import net.pkhapps.idispatch.gis.api.lookup.code.AddressPointClass;
import net.pkhapps.idispatch.gis.grpc.proto.GIS;
import net.pkhapps.idispatch.gis.grpc.util.ConversionUtil;
import org.jetbrains.annotations.NotNull;
import org.locationtech.jts.geom.Point;

import java.util.Locale;
import java.util.Optional;

/**
 * TODO Document me!
 */
class AddressPointImpl extends LocationFeatureImpl<Point> implements AddressPoint {

    private final AddressPointClass addressPointClass;
    private final String number;
    private final Point location;
    private final NamedFeature name;

    public AddressPointImpl(@NotNull GIS.LocationFeature message) {
        super(message);
        var ap = message.getAddressPoint();
        addressPointClass = AddressPointClass.valueOf(ap.getAddressPointClass());
        number = ap.hasNumber() ? ap.getNumber().getValue() : null;
        location = ConversionUtil.fromMessage(ap.getLocation());
        name = ConversionUtil.fromMessage(ap.getName());
    }

    @Override
    public @NotNull AddressPointClass getAddressPointClass() {
        return addressPointClass;
    }

    @Override
    public @NotNull Optional<String> getNumber() {
        return Optional.ofNullable(number);
    }

    @Override
    public @NotNull Point getLocation() {
        return location;
    }

    @Override
    public @NotNull Optional<String> getName(@NotNull Locale locale) {
        return name.getName(locale);
    }
}
