package net.pkhapps.idispatch.core.client.address;

import net.pkhapps.idispatch.gis.api.lookup.code.MunicipalityCode;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.locationtech.jts.geom.Point;

import java.util.Locale;
import java.util.Optional;

/**
 * TODO Document me!
 */
public class NamedAddress extends Address {

    private final String nameFin;
    private final String nameSwe;
    private final String number;

    public NamedAddress(@NotNull Point location, @Nullable MunicipalityCode municipality,
                        @Nullable String nameFin, @Nullable String nameSwe, @Nullable String number) {
        super(location, municipality);
        this.nameFin = nameFin;
        this.nameSwe = nameSwe;
        this.number = number;
    }

    public @NotNull String getName(@NotNull Locale locale) {
        if (locale.getLanguage().equals("fi") && StringUtils.isNotBlank(nameFin)) {
            return nameFin;
        } else if (locale.getLanguage().equals("sv") && StringUtils.isNotBlank(nameSwe)) {
            return nameSwe;
        } else {
            return StringUtils.firstNonEmpty(nameFin, nameSwe);
        }
    }

    public @NotNull Optional<String> getNumber() {
        return Optional.ofNullable(number);
    }
}
