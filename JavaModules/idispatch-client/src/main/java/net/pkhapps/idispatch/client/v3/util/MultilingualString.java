package net.pkhapps.idispatch.client.v3.util;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.Immutable;
import javax.annotation.concurrent.NotThreadSafe;
import java.io.Serializable;
import java.util.*;

/**
 * Class that represents a string in many languages.
 */
@Immutable
@SuppressWarnings("WeakerAccess")
public class MultilingualString implements Serializable {

    private Map<Locale, String> values;

    public MultilingualString() {
        this(Collections.emptyMap());
    }

    public MultilingualString(@Nonnull Map<Locale, String> values) {
        Objects.requireNonNull(values, "values must not be null");
        this.values = new HashMap<>(values);
    }

    public MultilingualString(@Nonnull Locale locale, @Nonnull String value) {
        this(Map.of(locale, value));
    }

    @Nonnull
    public MultilingualString withValue(@Nonnull Locale locale, @Nullable String value) {
        return new Builder(this).withValue(locale, value).build();
    }

    @Nonnull
    public MultilingualString withoutValue(@Nonnull Locale locale) {
        return withValue(locale, null);
    }

    @Nonnull
    public String value(@Nonnull Locale locale) {
        return values.getOrDefault(locale, "");
    }

    public boolean hasValue(@Nonnull Locale locale) {
        return values.containsKey(locale);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MultilingualString that = (MultilingualString) o;
        return Objects.equals(values, that.values);
    }

    @Override
    public int hashCode() {
        return Objects.hash(values);
    }

    /**
     * Builder for creating new instances of {@link MultilingualString} using a fluent API.
     */
    @NotThreadSafe
    public static class Builder {

        private final Map<Locale, String> values = new HashMap<>();

        public Builder() {
        }

        public Builder(@Nonnull MultilingualString original) {
            Objects.requireNonNull(original, "original must not be null");
            values.putAll(original.values);
        }

        @Nonnull
        public Builder withValue(@Nonnull Locale locale, @Nullable String value) {
            Objects.requireNonNull(locale, "locale must not be null");
            if (value == null) {
                values.remove(locale);
            } else {
                values.put(locale, value);
            }
            return this;
        }

        @Nonnull
        public MultilingualString build() {
            return new MultilingualString(values);
        }
    }
}
