package net.pkhapps.idispatch.gis.postgis.lookup;

import net.pkhapps.idispatch.gis.api.Locales;
import net.pkhapps.idispatch.gis.api.lookup.LocationFeature;
import net.pkhapps.idispatch.gis.api.lookup.NamedFeature;
import net.pkhapps.idispatch.gis.api.lookup.code.LocationAccuracy;
import net.pkhapps.idispatch.gis.api.lookup.code.MunicipalityCode;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.locationtech.jts.geom.Geometry;

import java.time.LocalDate;
import java.util.Locale;
import java.util.Optional;

import static java.util.Objects.requireNonNull;

/**
 * TODO Document me
 *
 * @param <Location>
 */
abstract class LocationFeatureImpl<Location extends Geometry> implements LocationFeature<Location>,
        NamedFeature {

    private final LocationAccuracy locationAccuracy;
    private final LocalDate validFrom;
    private final LocalDate validTo;
    private final Location location;
    private final MunicipalityCode municipality;
    private final String nameSv;
    private final String nameFi;
    private final String nameSe;
    private final String nameSmn;
    private final String nameSms;

    LocationFeatureImpl(@NotNull LocationAccuracy locationAccuracy,
                        @Nullable LocalDate validFrom,
                        @Nullable LocalDate validTo,
                        @Nullable Location location,
                        @Nullable MunicipalityCode municipality,
                        @Nullable String nameSv,
                        @Nullable String nameFi,
                        @Nullable String nameSe,
                        @Nullable String nameSmn,
                        @Nullable String nameSms) {
        this.locationAccuracy = requireNonNull(locationAccuracy);
        this.validFrom = validFrom;
        this.validTo = validTo;
        this.location = location;
        this.municipality = municipality;
        this.nameSv = nameSv;
        this.nameFi = nameFi;
        this.nameSe = nameSe;
        this.nameSmn = nameSmn;
        this.nameSms = nameSms;
    }

    @Override
    public @NotNull LocationAccuracy getLocationAccuracy() {
        return locationAccuracy;
    }

    @Override
    public @NotNull Optional<LocalDate> validFrom() {
        return Optional.ofNullable(validFrom);
    }

    @Override
    public @NotNull Optional<LocalDate> validTo() {
        return Optional.ofNullable(validTo);
    }

    @Override
    public @NotNull Optional<Location> getLocation() {
        return Optional.ofNullable(location);
    }

    @Override
    public @NotNull Optional<MunicipalityCode> getMunicipality() {
        return Optional.ofNullable(municipality);
    }

    @Override
    public @NotNull Optional<String> getName(@NotNull Locale locale) {
        if (Locales.FINNISH.getLanguage().equals(locale.getLanguage())) {
            return Optional.ofNullable(nameFi);
        } else if (Locales.SWEDISH.getLanguage().equals(locale.getLanguage())) {
            return Optional.ofNullable(nameSv);
        } else if (Locales.NORTHERN_SAMI.getLanguage().equals(locale.getLanguage())) {
            return Optional.ofNullable(nameSe);
        } else if (Locales.INARI_SAMI.getLanguage().equals(locale.getLanguage())) {
            return Optional.ofNullable(nameSmn);
        } else if (Locales.SKOLT_SAMI.getLanguage().equals(locale.getLanguage())) {
            return Optional.ofNullable(nameSms);
        }
        return Optional.empty();
    }
}
