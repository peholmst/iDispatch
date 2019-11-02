package net.pkhapps.idispatch.gis.api.lookup;

import net.pkhapps.idispatch.gis.api.lookup.code.LocationAccuracy;
import net.pkhapps.idispatch.gis.api.lookup.code.MunicipalityCode;
import org.jetbrains.annotations.NotNull;
import org.locationtech.jts.geom.Geometry;

import java.time.LocalDate;
import java.util.Optional;

/**
 * A feature, such as a road segment, an address point or some other place that has a clearly defined geographical
 * location.
 *
 * @param <Location> the type of the location data
 */
public interface LocationFeature<Location extends Geometry> {

    /**
     * The accuracy of the location data
     */
    @NotNull LocationAccuracy getLocationAccuracy();

    /**
     * The date from which the feature data is valid
     */
    @NotNull Optional<LocalDate> validFrom();

    /**
     * The date to which the feature data is valid
     */
    @NotNull Optional<LocalDate> validTo();

    /**
     * Returns whether the feature data is valid on the given date
     *
     * @param date the date to check
     * @return true if the data is valid on the given date, false otherwise
     */
    default boolean isDataValidOnDate(@NotNull LocalDate date) {
        if (validFrom().isPresent() && date.isBefore(validFrom().get())) {
            return false;
        }
        if (validTo().isPresent() && date.isAfter(validTo().get())) {
            return false;
        }
        return true;
    }

    /**
     * The location of this feature (in ETRS89 / TM35FIN)
     */
    @NotNull Location getLocation();

    /**
     * The municipality that the feature resides in, if applicable
     */
    @NotNull Optional<MunicipalityCode> getMunicipality();
}
