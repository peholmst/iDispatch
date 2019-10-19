package net.pkhapps.idispatch.gis.api.spi;

import net.pkhapps.idispatch.gis.api.lookup.LocationFeatureLookupService;
import net.pkhapps.idispatch.gis.api.lookup.MunicipalityLookupService;
import org.jetbrains.annotations.NotNull;

/**
 * TODO document me!
 */
public interface GIS {

    @NotNull MunicipalityLookupService getMunicipalityLookupService();

    @NotNull LocationFeatureLookupService getLocationFeatureLookupService();
}
