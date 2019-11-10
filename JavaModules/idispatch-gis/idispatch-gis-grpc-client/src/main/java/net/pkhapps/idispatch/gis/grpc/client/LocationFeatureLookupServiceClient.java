package net.pkhapps.idispatch.gis.grpc.client;

import io.grpc.Channel;
import net.pkhapps.idispatch.gis.api.lookup.LocationFeature;
import net.pkhapps.idispatch.gis.api.lookup.LocationFeatureLookupService;
import net.pkhapps.idispatch.gis.api.lookup.NameMatchStrategy;
import net.pkhapps.idispatch.gis.api.lookup.code.MunicipalityCode;
import net.pkhapps.idispatch.gis.grpc.proto.GIS;
import net.pkhapps.idispatch.gis.grpc.proto.LocationFeatureLookupServiceGrpc;
import net.pkhapps.idispatch.gis.grpc.util.ConversionUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.locationtech.jts.geom.Point;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * TODO Document me
 */
class LocationFeatureLookupServiceClient implements LocationFeatureLookupService {

    private static final Logger LOGGER = LoggerFactory.getLogger(MunicipalityLookupServiceClient.class);
    private final LocationFeatureLookupServiceGrpc.LocationFeatureLookupServiceBlockingStub stub;

    LocationFeatureLookupServiceClient(@NotNull Channel channel) {
        stub = LocationFeatureLookupServiceGrpc.newBlockingStub(channel);
    }

    @Override
    public @NotNull List<LocationFeature<?>> findFeaturesCloseToPoint(@NotNull Point point) {
        try {
            var result = stub.findFeaturesCloseToPoint(ConversionUtil.toMessage(point));
            return ConversionUtil.toStream(result)
                    .map(this::fromMessage)
                    .collect(Collectors.toList());
        } catch (Exception ex) {
            LOGGER.error("Error finding features close to point", ex);
            return Collections.emptyList();
        }
    }

    @Override
    public @NotNull List<LocationFeature<?>> findFeaturesByName(@Nullable MunicipalityCode municipality,
                                                                @NotNull String name,
                                                                @NotNull NameMatchStrategy matchBy,
                                                                @Nullable String number) {
        try {
            var requestBuilder = GIS.LocationFeatureSearchRequest.newBuilder();
            if (municipality != null) {
                requestBuilder.setMunicipality(ConversionUtil.toMessage(municipality));
            }
            requestBuilder.setNameSearch(GIS.SearchRequest.newBuilder()
                    .setSearchTerm(name)
                    .setMatchStrategy(GIS.MatchStrategy.valueOf(matchBy.name()))
                    .build());
            if (number != null) {
                requestBuilder.setNumber(ConversionUtil.wrap(number));
            }
            var result = stub.findFeaturesByName(requestBuilder.build());
            return ConversionUtil.toStream(result)
                    .map(this::fromMessage)
                    .collect(Collectors.toList());
        } catch (Exception ex) {
            LOGGER.error("Error finding features by name", ex);
            return Collections.emptyList();
        }
    }

    private @NotNull LocationFeature<?> fromMessage(@NotNull GIS.LocationFeature message) {
        if (message.hasAddressPoint()) {
            return new AddressPointImpl(message);
        } else if (message.hasRoadSegment()) {
            return new RoadSegmentImpl(message);
        } else {
            throw new IllegalArgumentException("Unsupported LocationFeature");
        }
    }
}
