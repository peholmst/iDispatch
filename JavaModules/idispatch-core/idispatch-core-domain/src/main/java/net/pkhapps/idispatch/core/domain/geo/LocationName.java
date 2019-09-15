package net.pkhapps.idispatch.core.domain.geo;

import net.pkhapps.idispatch.core.domain.i18n.MultilingualString;
import org.jetbrains.annotations.NotNull;

import java.util.Locale;

/**
 * Value object representing the name of a specific location.
 */
public class LocationName extends MultilingualString {

    public LocationName(@NotNull Locale locale, @NotNull String value) {
        super(locale, value);
    }
}
