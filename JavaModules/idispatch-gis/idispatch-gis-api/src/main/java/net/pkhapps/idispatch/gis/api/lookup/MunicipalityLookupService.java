package net.pkhapps.idispatch.gis.api.lookup;

import org.jetbrains.annotations.NotNull;
import org.opengis.geometry.DirectPosition;

import java.util.List;
import java.util.Optional;

/**
 * A service for looking up municipalities in different ways.
 */
public interface MunicipalityLookupService {

    /**
     * Looks up the municipality that the given position is located within.
     *
     * @param position a geographical position with a non-null {@link DirectPosition#getCoordinateReferenceSystem() CRS}
     * @return the municipality if the position happens to be inside one, otherwise an empty {@code Optional}
     * @throws IllegalArgumentException if the position has an unsupported CRS or none at all
     */
    @NotNull
    Optional<Municipality> findMunicipalityOfPosition(@NotNull DirectPosition position);

    /**
     * Looks up the municipality with the given national code.
     *
     * @param nationalCode a national municipality code
     * @return the municipality if found, otherwise an empty {@code Optional}
     */
    @NotNull
    Optional<Municipality> findByNationalCode(@NotNull String nationalCode);

    /**
     * Looks up the municipality whose name (in any language) matches the given search term.
     *
     * @param name    a name or part of the name to search for
     * @param matchBy the strategy for how the name should be matched
     * @return a list of municipalities that match the search criteria
     */
    @NotNull
    List<Municipality> findByName(@NotNull String name, @NotNull NameMatchStrategy matchBy);

    /**
     * Enumeration of different strategies for matching a search term to a municipality name.
     */
    enum NameMatchStrategy {
        /**
         * Look for municipalities that have a name in any language that starts with the search term (case insensitive).
         */
        PREFIX,
        /**
         * Look for municipalities that have a name in any language that is exactly equal to the search term (case
         * insensitive).
         */
        EXACT,
    }
}
