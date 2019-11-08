package net.pkhapps.idispatch.gis.grpc.client;

import io.grpc.Channel;
import net.pkhapps.idispatch.gis.api.lookup.Municipality;
import net.pkhapps.idispatch.gis.api.lookup.MunicipalityLookupService;
import net.pkhapps.idispatch.gis.api.lookup.NameMatchStrategy;
import net.pkhapps.idispatch.gis.grpc.proto.GIS;
import net.pkhapps.idispatch.gis.grpc.proto.MunicipalityLookupServiceGrpc;
import org.jetbrains.annotations.NotNull;
import org.locationtech.jts.geom.Point;

import java.util.List;
import java.util.Optional;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * TODO Document me
 */
public class MunicipalityLookupServiceImpl implements MunicipalityLookupService {

    private final MunicipalityLookupServiceGrpc.MunicipalityLookupServiceBlockingStub stub;

    public MunicipalityLookupServiceImpl(@NotNull Channel channel) {
        stub = MunicipalityLookupServiceGrpc.newBlockingStub(channel);
    }

    @Override
    public @NotNull Optional<Municipality> findMunicipalityOfPoint(@NotNull Point point) {
        return Optional.ofNullable(stub.findMunicipalityOfPoint(GIS.Point.newBuilder()
                .setSrid(point.getSRID())
                .setCoordinate(GIS.Coordinate.newBuilder()
                        .setX(point.getX())
                        .setY(point.getY())
                        .build())
                .build())).map(MunicipalityImpl::new);
    }

    @Override
    public @NotNull Optional<Municipality> findByNationalCode(@NotNull String nationalCode) {
        return Optional.ofNullable(stub.findByNationalCode(GIS.SearchRequest.newBuilder()
                .setSearchTerm(nationalCode)
                .build())).map(MunicipalityImpl::new);
    }

    @Override
    public @NotNull List<Municipality> findByName(@NotNull String name, @NotNull NameMatchStrategy matchBy) {
        var result = stub.findByName(GIS.SearchRequest.newBuilder()
                .setSearchTerm(name)
                .setMatchStrategy(GIS.MatchStrategy.valueOf(matchBy.name()))
                .build());
        return StreamSupport.stream(Spliterators.spliteratorUnknownSize(result, Spliterator.NONNULL), false)
                .map(MunicipalityImpl::new)
                .collect(Collectors.toList());
    }
}
