package net.pkhapps.idispatch.gis.grpc.client;

import net.pkhapps.idispatch.gis.api.lookup.LocationFeature;
import net.pkhapps.idispatch.gis.api.lookup.code.LocationAccuracy;
import net.pkhapps.idispatch.gis.api.lookup.code.MunicipalityCode;
import net.pkhapps.idispatch.gis.grpc.proto.GIS;
import net.pkhapps.idispatch.gis.grpc.util.ConversionUtil;
import org.jetbrains.annotations.NotNull;
import org.locationtech.jts.geom.Geometry;

import java.time.LocalDate;
import java.util.Optional;

/**
 * TODO Document me
 *
 * @param <Location>
 */
abstract class LocationFeatureImpl<Location extends Geometry> implements LocationFeature<Location> {

    private final LocationAccuracy locationAccuracy;
    private final LocalDate validFrom;
    private final LocalDate validTo;
    private final MunicipalityCode municipalityCode;

    LocationFeatureImpl(@NotNull GIS.LocationFeature message) {
        locationAccuracy = LocationAccuracy.valueOf(message.getLocationAccuracy());
        validFrom = message.hasValidFrom() ? ConversionUtil.fromMessage(message.getValidFrom()) : null;
        validTo = message.hasValidTo() ? ConversionUtil.fromMessage(message.getValidTo()) : null;
        municipalityCode = message.hasMunicipality() ? ConversionUtil.fromMessage(message.getMunicipality()) : null;
    }

    @Override
    public @NotNull LocationAccuracy getLocationAccuracy() {
        return locationAccuracy;
    }

    @Override
    public @NotNull Optional<LocalDate> getValidFrom() {
        return Optional.ofNullable(validFrom);
    }

    @Override
    public @NotNull Optional<LocalDate> getValidTo() {
        return Optional.ofNullable(validTo);
    }

    @Override
    public @NotNull Optional<MunicipalityCode> getMunicipality() {
        return Optional.ofNullable(municipalityCode);
    }
}
