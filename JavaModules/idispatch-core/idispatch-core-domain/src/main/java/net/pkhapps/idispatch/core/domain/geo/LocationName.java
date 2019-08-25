package net.pkhapps.idispatch.core.domain.geo;

import net.pkhapps.idispatch.core.domain.common.ValueObject;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

import static java.util.Objects.requireNonNull;

/**
 * TODO Implement me!
 */
public class LocationName implements ValueObject {

    public LocationName() {
    }

    public LocationName(@NotNull Essence essence) {
    }

    public LocationName(@NotNull Locale locale, @NotNull String name) {
    }

    public boolean hasLocale(@NotNull Locale locale) {
        return false;
    }

    public @NotNull Stream<Locale> locales() {
        return Stream.empty();
    }

    public @NotNull Optional<String> localized(@NotNull Locale locale) {
        return Optional.empty();
    }

    public @NotNull String defaultName() {
        return "";
    }

    // TODO equals and hashCode

    /**
     *
     */
    public static class Essence {

        private final Map<Locale, String> localizedNames = new HashMap<>();

        @NotNull
        public Optional<String> getName(@NotNull Locale locale) {
            requireNonNull(locale);
            return Optional.ofNullable(localizedNames.get(locale));
        }

        public void setName(@NotNull Locale locale, @Nullable String name) {
            requireNonNull(locale);
            if (name == null) {
                localizedNames.remove(locale);
            } else {
                localizedNames.put(locale, name);
            }
        }
    }

}
