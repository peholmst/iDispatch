package net.pkhapps.idispatch.core.domain.i18n;

import net.pkhapps.idispatch.core.domain.common.ValueObject;
import org.jetbrains.annotations.NotNull;

import java.util.Locale;
import java.util.Map;
import java.util.Objects;

import static java.util.Objects.requireNonNull;

/**
 * Value object representing a string in multiple languages. It is guaranteed to always contain a string in at least
 * one language, i.e. it is impossible to create completely empty multilingual strings.
 */
public class MultilingualString implements ValueObject {

    private final Map<Locale, String> values;

    /**
     * Creates a new multilingual string with only one locale and value.
     *
     * @param locale the locale of the value.
     * @param value  the value itself.
     */
    public MultilingualString(@NotNull Locale locale, @NotNull String value) {
        this.values = Map.of(requireNonNull(locale), requireNonNull(value));
    }

    /**
     * Creates a new multilingual string with the given locales and values.
     *
     * @param values a map containing at least one locale-value pair.
     */
    public MultilingualString(@NotNull Map<Locale, String> values) {
        if (values.isEmpty()) {
            throw new IllegalArgumentException("At least one locale-value pair must be provided");
        }
        this.values = Map.copyOf(values);
    }

    /**
     * Returns the multilingual string as an unmodifiable map keyed by locale.
     */
    public final @NotNull Map<Locale, String> toMap() {
        return values;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MultilingualString that = (MultilingualString) o;
        return values.equals(that.values);
    }

    @Override
    public int hashCode() {
        return Objects.hash(values);
    }
}
