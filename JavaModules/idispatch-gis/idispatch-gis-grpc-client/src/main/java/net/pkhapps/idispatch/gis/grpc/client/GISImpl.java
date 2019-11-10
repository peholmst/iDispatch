package net.pkhapps.idispatch.gis.grpc.client;

import io.grpc.Channel;
import net.pkhapps.idispatch.gis.api.lookup.LocationFeatureLookupService;
import net.pkhapps.idispatch.gis.api.lookup.MunicipalityLookupService;
import net.pkhapps.idispatch.gis.api.spi.GIS;
import org.jetbrains.annotations.NotNull;

/**
 * TODO Document me
 */
class GISImpl implements GIS {

    private final MunicipalityLookupServiceClient municipalityLookupService;
    private final LocationFeatureLookupService locationFeatureLookupService;

    GISImpl(@NotNull Channel channel) {
        municipalityLookupService = new MunicipalityLookupServiceClient(channel);
        locationFeatureLookupService = new LocationFeatureLookupServiceClient(channel);
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
