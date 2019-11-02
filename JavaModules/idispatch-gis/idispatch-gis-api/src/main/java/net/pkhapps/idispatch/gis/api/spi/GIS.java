package net.pkhapps.idispatch.gis.api.spi;

import net.pkhapps.idispatch.gis.api.lookup.LocationFeatureLookupService;
import net.pkhapps.idispatch.gis.api.lookup.MunicipalityLookupService;
import org.jetbrains.annotations.NotNull;

/**
 * Provides access to the iDispatch GIS services.
 *
 * @see GISFactory
 */
public interface GIS {

    /**
     * The {@link MunicipalityLookupService} for looking up municipalities in different ways
     */
    @NotNull MunicipalityLookupService getMunicipalityLookupService();

    /**
     * The {@link LocationFeatureLookupService} for looking up different location features such as address points or
     * road segments
     */
    @NotNull LocationFeatureLookupService getLocationFeatureLookupService();
}
