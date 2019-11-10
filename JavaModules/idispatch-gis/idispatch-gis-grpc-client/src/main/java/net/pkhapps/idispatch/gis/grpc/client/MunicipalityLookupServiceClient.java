package net.pkhapps.idispatch.gis.grpc.client;

import io.grpc.Channel;
import net.pkhapps.idispatch.gis.api.lookup.Municipality;
import net.pkhapps.idispatch.gis.api.lookup.MunicipalityLookupService;
import net.pkhapps.idispatch.gis.api.lookup.NameMatchStrategy;
import net.pkhapps.idispatch.gis.grpc.proto.GIS;
import net.pkhapps.idispatch.gis.grpc.proto.MunicipalityLookupServiceGrpc;
import net.pkhapps.idispatch.gis.grpc.util.ConversionUtil;
import org.jetbrains.annotations.NotNull;
import org.locationtech.jts.geom.Point;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * TODO Document me
 */
class MunicipalityLookupServiceClient implements MunicipalityLookupService {

    private static final Logger LOGGER = LoggerFactory.getLogger(MunicipalityLookupServiceClient.class);
    private final MunicipalityLookupServiceGrpc.MunicipalityLookupServiceBlockingStub stub;

    MunicipalityLookupServiceClient(@NotNull Channel channel) {
        stub = MunicipalityLookupServiceGrpc.newBlockingStub(channel);
    }

    @Override
    public @NotNull Optional<Municipality> findMunicipalityOfPoint(@NotNull Point point) {
        try {
            return Optional.ofNullable(stub.findMunicipalityOfPoint(ConversionUtil.toMessage(point)))
                    .map(MunicipalityImpl::new);
        } catch (Exception ex) {
            LOGGER.error("Error finding municipality of point", ex);
            return Optional.empty();
        }
    }

    @Override
    public @NotNull Optional<Municipality> findByNationalCode(@NotNull String nationalCode) {
        try {
            return Optional.ofNullable(stub.findByNationalCode(GIS.SearchRequest.newBuilder()
                    .setSearchTerm(nationalCode)
                    .build())).map(MunicipalityImpl::new);
        } catch (Exception ex) {
            LOGGER.error("Error finding municipality by national code", ex);
            return Optional.empty();
        }
    }

    @Override
    public @NotNull List<Municipality> findByName(@NotNull String name, @NotNull NameMatchStrategy matchBy) {
        try {
            var result = stub.findByName(GIS.SearchRequest.newBuilder()
                    .setSearchTerm(name)
                    .setMatchStrategy(GIS.MatchStrategy.valueOf(matchBy.name()))
                    .build());
            return ConversionUtil.toStream(result)
                    .map(MunicipalityImpl::new)
                    .collect(Collectors.toList());
        } catch (Exception ex) {
            LOGGER.error("Error finding municipalities by name", ex);
            return Collections.emptyList();
        }
    }
}
