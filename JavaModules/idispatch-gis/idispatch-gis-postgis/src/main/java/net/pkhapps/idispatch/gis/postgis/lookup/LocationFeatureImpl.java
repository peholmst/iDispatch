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
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;

import static java.util.Objects.requireNonNull;

/**
 * Base class for {@link LocationFeature} implementations.
 *
 * @param <Location> the type of the location data
 */
abstract class LocationFeatureImpl<Location extends Geometry> implements LocationFeature<Location>,
        NamedFeature {

    private final LocationAccuracy locationAccuracy;
    private final LocalDate validFrom;
    private final LocalDate validTo;
    private final Location location;
    private final MunicipalityCode municipality;
    private final Map<String, String> name;

    LocationFeatureImpl(@NotNull LocationAccuracy locationAccuracy,
                        @Nullable LocalDate validFrom,
                        @Nullable LocalDate validTo,
                        @NotNull Location location,
                        @Nullable MunicipalityCode municipality,
                        @Nullable String nameSv,
                        @Nullable String nameFi,
                        @Nullable String nameSe,
                        @Nullable String nameSmn,
                        @Nullable String nameSms) {
        this.locationAccuracy = requireNonNull(locationAccuracy);
        this.validFrom = validFrom;
        this.validTo = validTo;
        this.location = requireNonNull(location);
        this.municipality = municipality;
        this.name = new HashMap<>();
        Optional.ofNullable(nameSv).ifPresent(n -> name.put(Locales.SWEDISH.getLanguage(), n));
        Optional.ofNullable(nameFi).ifPresent(n -> name.put(Locales.FINNISH.getLanguage(), n));
        Optional.ofNullable(nameSe).ifPresent(n -> name.put(Locales.NORTHERN_SAMI.getLanguage(), n));
        Optional.ofNullable(nameSmn).ifPresent(n -> name.put(Locales.INARI_SAMI.getLanguage(), n));
        Optional.ofNullable(nameSms).ifPresent(n -> name.put(Locales.SKOLT_SAMI.getLanguage(), n));
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
    public @NotNull Location getLocation() {
        return location;
    }

    @Override
    public @NotNull Optional<MunicipalityCode> getMunicipality() {
        return Optional.ofNullable(municipality);
    }

    @Override
    public @NotNull Optional<String> getName(@NotNull Locale locale) {
        return Optional.ofNullable(name.get(locale.getLanguage()));
    }

    void buildToString(@NotNull StringBuilder sb) {
        sb.append("locationAccuracy=").append(locationAccuracy).append(", ");
        sb.append("validFrom=").append(validFrom).append(", ");
        sb.append("validTo=").append(validTo).append(", ");
        sb.append("location=").append(location).append(", ");
        sb.append("municipality=").append(municipality).append(", ");
        sb.append("name=").append(name);
    }

    @Override
    public String toString() {
        var sb = new StringBuilder();
        buildToString(sb);
        return String.format("%s{%s}", getClass().getSimpleName(), sb);
    }
}
