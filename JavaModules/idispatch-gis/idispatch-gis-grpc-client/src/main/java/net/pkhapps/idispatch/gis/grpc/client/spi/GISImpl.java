package net.pkhapps.idispatch.gis.grpc.client.spi;

import io.grpc.Channel;
import net.pkhapps.idispatch.gis.api.lookup.LocationFeatureLookupService;
import net.pkhapps.idispatch.gis.api.lookup.MunicipalityLookupService;
import net.pkhapps.idispatch.gis.api.spi.GIS;
import net.pkhapps.idispatch.gis.grpc.client.MunicipalityLookupServiceImpl;
import org.jetbrains.annotations.NotNull;

/**
 * TODO Document me
 */
class GISImpl implements GIS {

    private final MunicipalityLookupServiceImpl municipalityLookupService;

    GISImpl(@NotNull Channel channel) {
        this.municipalityLookupService = new MunicipalityLookupServiceImpl(channel);
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
