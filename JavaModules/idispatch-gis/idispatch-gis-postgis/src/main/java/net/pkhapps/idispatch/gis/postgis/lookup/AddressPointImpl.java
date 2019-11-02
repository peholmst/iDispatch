package net.pkhapps.idispatch.gis.postgis.lookup;

import net.pkhapps.idispatch.gis.api.lookup.AddressPoint;
import net.pkhapps.idispatch.gis.api.lookup.code.AddressPointClass;
import net.pkhapps.idispatch.gis.api.lookup.code.LocationAccuracy;
import net.pkhapps.idispatch.gis.api.lookup.code.MunicipalityCode;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.locationtech.jts.geom.Point;

import java.time.LocalDate;
import java.util.Optional;

import static java.util.Objects.requireNonNull;

/**
 * TODO Document me
 */
class AddressPointImpl extends LocationFeatureImpl<Point> implements AddressPoint {

    private final AddressPointClass addressPointClass;
    private final String number;

    AddressPointImpl(@NotNull LocationAccuracy locationAccuracy,
                     @Nullable LocalDate validFrom,
                     @Nullable LocalDate validTo,
                     @Nullable Point point,
                     @Nullable MunicipalityCode municipality,
                     @Nullable String nameSv,
                     @Nullable String nameFi,
                     @Nullable String nameSe,
                     @Nullable String nameSmn,
                     @Nullable String nameSms,
                     @NotNull AddressPointClass addressPointClass,
                     @Nullable String number) {
        super(locationAccuracy, validFrom, validTo, point, municipality, nameSv, nameFi, nameSe, nameSmn, nameSms);
        this.addressPointClass = requireNonNull(addressPointClass);
        this.number = number;
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
    void buildToString(@NotNull StringBuilder sb) {
        super.buildToString(sb);
        sb.append(", ");
        sb.append("addressPointClass=").append(addressPointClass).append(", ");
        sb.append("number=").append(number);
    }
}
