package net.pkhapps.idispatch.gis.postgis.lookup;

import net.pkhapps.idispatch.gis.api.lookup.Municipality;
import org.jetbrains.annotations.NotNull;
import org.locationtech.jts.geom.Point;

import java.util.Locale;
import java.util.Optional;

import static java.util.Objects.requireNonNull;

/**
 * TODO Document me
 */
class MunicipalityImpl implements Municipality {

    private final String nationalCode;
    private final String nameFi;
    private final String nameSv;
    private final Point center;

    MunicipalityImpl(@NotNull String nationalCode, @NotNull String nameFi, @NotNull String nameSv,
                     @NotNull Point center) {
        this.nationalCode = requireNonNull(nationalCode);
        this.nameFi = requireNonNull(nameFi);
        this.nameSv = requireNonNull(nameSv);
        this.center = requireNonNull(center);
    }

    @Override
    public @NotNull String getNationalCode() {
        return nationalCode;
    }

    @Override
    public @NotNull Optional<String> getName(@NotNull Locale locale) {
        switch (locale.getLanguage()) {
            case "fi":
                return Optional.of(nameFi);
            case "sv":
                return Optional.of(nameSv);
        }
        return Optional.empty();
    }

    @Override
    public @NotNull Point getCenter() {
        return center;
    }
}
