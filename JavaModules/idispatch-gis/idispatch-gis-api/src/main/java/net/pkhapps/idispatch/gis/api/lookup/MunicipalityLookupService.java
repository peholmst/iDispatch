package net.pkhapps.idispatch.gis.api.lookup;

import org.jetbrains.annotations.NotNull;
import org.locationtech.jts.geom.Point;

import java.util.List;
import java.util.Optional;

/**
 * A service for looking up municipalities in different ways.
 */
public interface MunicipalityLookupService {

    /**
     * Looks up the municipality that the given point is located within.
     *
     * @param point a geographical point
     * @return the municipality if the point happens to be inside one, otherwise an empty {@code Optional}
     * @throws IllegalArgumentException if the point has an unsupported SRID or none at all
     */
    @NotNull
    Optional<Municipality> findMunicipalityOfPoint(@NotNull Point point);

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
}
