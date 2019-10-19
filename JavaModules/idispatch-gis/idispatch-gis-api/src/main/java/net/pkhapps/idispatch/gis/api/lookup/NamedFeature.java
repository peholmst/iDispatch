package net.pkhapps.idispatch.gis.api.lookup;

import org.jetbrains.annotations.NotNull;

import java.util.Locale;
import java.util.Optional;

/**
 * TODO Document me
 */
public interface NamedFeature {

    /**
     * Gets the name of the feature in the given language.
     *
     * @param locale the language/locale to fetch the feature name in
     * @return the name of the feature or an empty {@code Optional} if the feature has no name in the given
     * language
     */
    @NotNull Optional<String> getName(@NotNull Locale locale);
}
