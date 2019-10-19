package net.pkhapps.idispatch.gis.postgis.spi;

import net.pkhapps.idispatch.gis.api.lookup.LocationFeatureLookupService;
import net.pkhapps.idispatch.gis.api.lookup.MunicipalityLookupService;
import net.pkhapps.idispatch.gis.api.spi.GIS;
import net.pkhapps.idispatch.gis.postgis.lookup.MunicipalityLookupServiceImpl;
import org.jetbrains.annotations.NotNull;

import javax.sql.DataSource;

/**
 * TODO Document me!
 */
class GISImpl implements GIS {

    private final MunicipalityLookupService municipalityLookupService;

    GISImpl(@NotNull DataSource dataSource) {
        municipalityLookupService = new MunicipalityLookupServiceImpl(dataSource);
    }

    @Override
    public @NotNull MunicipalityLookupService getMunicipalityLookupService() {
        return municipalityLookupService;
    }

    @Override
    public @NotNull LocationFeatureLookupService getLocationFeatureLookupService() {
        throw new UnsupportedOperationException("Not implemented yet");
    }
}
