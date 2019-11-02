package net.pkhapps.idispatch.gis.api.lookup;

import net.pkhapps.idispatch.gis.api.lookup.code.MunicipalityCode;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.locationtech.jts.geom.Point;

import java.util.List;

/**
 * A service for looking up {@linkplain LocationFeature location features}, such as address points or road segments,
 * in different ways.
 *
 * @see AddressPoint
 * @see RoadSegment
 */
public interface LocationFeatureLookupService {

    /**
     * Looks up all location features that are close to the given point. The implementation may decide what "close to"
     * means in this context.
     *
     * @param point a geographical point in either ETRS89 / TM35FIN or WGS84
     * @return a list of features
     * @throws IllegalArgumentException if the point has an unsupported SRID or none at all
     */
    @NotNull
    List<LocationFeature<?>> findFeaturesCloseToPoint(@NotNull Point point);

    /**
     * Looks up all location features whose name (in any language) matches the given search term.
     *
     * @param municipality the municipality to limit the search to or {@code null} to look for features in any
     *                     municipality
     * @param name         the name or part thereof to search for
     * @param matchBy      the strategy for how the name should be matched
     * @param number       the address number to search for if applicable
     * @return a list of features that match the search criteria
     */
    @NotNull
    List<LocationFeature<?>> findFeaturesByName(@Nullable MunicipalityCode municipality, @NotNull String name,
                                                @NotNull NameMatchStrategy matchBy, @Nullable String number);
}
