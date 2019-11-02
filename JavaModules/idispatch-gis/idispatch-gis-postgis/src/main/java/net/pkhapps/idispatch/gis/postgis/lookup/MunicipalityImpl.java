package net.pkhapps.idispatch.gis.postgis.lookup;

import net.pkhapps.idispatch.gis.api.Locales;
import net.pkhapps.idispatch.gis.api.lookup.Municipality;
import net.pkhapps.idispatch.gis.api.lookup.code.MunicipalityCode;
import org.jetbrains.annotations.NotNull;
import org.locationtech.jts.geom.Point;

import java.util.Locale;
import java.util.Optional;

import static java.util.Objects.requireNonNull;

/**
 * Implementation of {@link Municipality}.
 */
class MunicipalityImpl implements Municipality {

    private final MunicipalityCode nationalCode;
    private final String nameFi;
    private final String nameSv;
    private final Point center;

    MunicipalityImpl(@NotNull String nationalCode, @NotNull String nameFi, @NotNull String nameSv,
                     @NotNull Point center) {
        this.nationalCode = MunicipalityCode.of(requireNonNull(nationalCode));
        this.nameFi = requireNonNull(nameFi);
        this.nameSv = requireNonNull(nameSv);
        this.center = requireNonNull(center);
    }

    @Override
    public @NotNull MunicipalityCode getNationalCode() {
        return nationalCode;
    }

    @Override
    public @NotNull Optional<String> getName(@NotNull Locale locale) {
        if (Locales.FINNISH.getLanguage().equals(locale.getLanguage())) {
            return Optional.of(nameFi);
        } else if (Locales.SWEDISH.getLanguage().equals(locale.getLanguage())) {
            return Optional.of(nameSv);
        }
        return Optional.empty();
    }

    @Override
    public @NotNull Point getCenter() {
        return center;
    }
}
