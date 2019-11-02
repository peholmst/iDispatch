package net.pkhapps.idispatch.gis.postgis.spi;

import net.pkhapps.idispatch.gis.api.lookup.LocationFeatureLookupService;
import net.pkhapps.idispatch.gis.api.lookup.MunicipalityLookupService;
import net.pkhapps.idispatch.gis.api.spi.GIS;
import net.pkhapps.idispatch.gis.postgis.lookup.LocationFeatureLookupServiceImpl;
import net.pkhapps.idispatch.gis.postgis.lookup.MunicipalityLookupServiceImpl;
import org.jetbrains.annotations.NotNull;

import javax.sql.DataSource;

/**
 * Implementation of {@link GIS} created by {@link GISFactoryImpl}.
 */
class GISImpl implements GIS {

    private final MunicipalityLookupService municipalityLookupService;
    private final LocationFeatureLookupService locationFeatureLookupService;

    GISImpl(@NotNull DataSource dataSource) {
        municipalityLookupService = new MunicipalityLookupServiceImpl(dataSource);
        locationFeatureLookupService = new LocationFeatureLookupServiceImpl(dataSource);
    }

    @Override
    public @NotNull MunicipalityLookupService getMunicipalityLookupService() {
        return municipalityLookupService;
    }

    @Override
    public @NotNull LocationFeatureLookupService getLocationFeatureLookupService() {
        return locationFeatureLookupService;
    }
}
