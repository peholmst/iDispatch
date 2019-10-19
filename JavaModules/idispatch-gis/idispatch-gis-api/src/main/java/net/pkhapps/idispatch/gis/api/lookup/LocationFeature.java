package net.pkhapps.idispatch.gis.api.lookup;

import net.pkhapps.idispatch.gis.api.lookup.code.LocationAccuracy;
import net.pkhapps.idispatch.gis.api.lookup.code.MunicipalityCode;
import org.jetbrains.annotations.NotNull;
import org.locationtech.jts.geom.Geometry;

import java.time.LocalDate;
import java.util.Optional;

/**
 * TODO Document me!
 *
 * @param <Location>
 */
public interface LocationFeature<Location extends Geometry> {

    @NotNull LocationAccuracy getLocationAccuracy();

    @NotNull Optional<LocalDate> validFrom();

    @NotNull Optional<LocalDate> validTo();

    @NotNull Optional<Location> getLocation();

    @NotNull Optional<MunicipalityCode> getMunicipality();
}
