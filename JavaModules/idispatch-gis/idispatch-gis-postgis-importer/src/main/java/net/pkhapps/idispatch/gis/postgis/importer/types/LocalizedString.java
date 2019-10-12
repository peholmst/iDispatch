package net.pkhapps.idispatch.gis.postgis.importer.types;

import org.jetbrains.annotations.NotNull;

import java.util.Locale;
import java.util.Objects;

/**
 * TODO Document me!
 */
public class LocalizedString {

    private final Locale locale;
    private final String text;

    public LocalizedString(@NotNull Locale locale, @NotNull String text) {
        this.locale = locale;
        this.text = text;
    }

    @NotNull
    public Locale getLocale() {
        return locale;
    }

    @NotNull
    public String getText() {
        return text;
    }

    @Override
    public String toString() {
        return String.format("%s{locale=%s, text=%s}", getClass().getSimpleName(), locale, text);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LocalizedString that = (LocalizedString) o;
        return locale.equals(that.locale) &&
                text.equals(that.text);
    }

    @Override
    public int hashCode() {
        return Objects.hash(locale, text);
    }
}
