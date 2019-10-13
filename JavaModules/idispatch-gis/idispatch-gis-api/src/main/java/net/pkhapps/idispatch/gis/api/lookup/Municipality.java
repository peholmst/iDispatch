package net.pkhapps.idispatch.gis.api.lookup;

import org.jetbrains.annotations.NotNull;
import org.locationtech.jts.geom.Point;

import java.util.Locale;
import java.util.Optional;

/**
 * Holds the national code and the names in all applicable languages of a single municipality.
 */
public interface Municipality {

    /**
     * Gets the national code of the municipality.
     *
     * @return the national code
     */
    @NotNull
    String getNationalCode();

    /**
     * Gets the name of the municipality in the given language.
     *
     * @param locale the language/locale to fetch the municipality name in
     * @return the name of the municipality or an empty {@code Optional} if the municipality has no name in the given
     * language
     */
    @NotNull
    Optional<String> getName(@NotNull Locale locale);

    /**
     * Gets the center point of the municipality.
     *
     * @return the center point
     */
    @NotNull
    Point getCenter();
}
