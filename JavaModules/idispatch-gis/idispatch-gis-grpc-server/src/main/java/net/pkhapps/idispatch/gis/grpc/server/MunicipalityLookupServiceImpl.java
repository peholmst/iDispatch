package net.pkhapps.idispatch.gis.grpc.server;

import io.grpc.stub.StreamObserver;
import net.pkhapps.idispatch.gis.api.lookup.Municipality;
import net.pkhapps.idispatch.gis.api.lookup.MunicipalityLookupService;
import net.pkhapps.idispatch.gis.api.lookup.NameMatchStrategy;
import net.pkhapps.idispatch.gis.grpc.proto.GIS;
import net.pkhapps.idispatch.gis.grpc.proto.MunicipalityLookupServiceGrpc;
import net.pkhapps.idispatch.gis.grpc.util.ConversionUtil;
import org.jetbrains.annotations.NotNull;

/**
 * TODO Document me
 */
class MunicipalityLookupServiceImpl extends MunicipalityLookupServiceGrpc.MunicipalityLookupServiceImplBase {

    private final MunicipalityLookupService municipalityLookupService;

    MunicipalityLookupServiceImpl(@NotNull MunicipalityLookupService municipalityLookupService) {
        this.municipalityLookupService = municipalityLookupService;
    }

    @Override
    public void findMunicipalityOfPoint(GIS.Point request, StreamObserver<GIS.Municipality> responseObserver) {
        this.municipalityLookupService
                .findMunicipalityOfPoint(ConversionUtil.fromMessage(request))
                .map(this::toMessage)
                .ifPresent(responseObserver::onNext);
        responseObserver.onCompleted();
    }

    @Override
    public void findByNationalCode(GIS.SearchRequest request, StreamObserver<GIS.Municipality> responseObserver) {
        municipalityLookupService
                .findByNationalCode(request.getSearchTerm())
                .map(this::toMessage)
                .ifPresent(responseObserver::onNext);
        responseObserver.onCompleted();
    }

    @Override
    public void findByName(GIS.SearchRequest request, StreamObserver<GIS.Municipality> responseObserver) {
        municipalityLookupService
                .findByName(request.getSearchTerm(), NameMatchStrategy.valueOf(request.getMatchStrategy().name()))
                .stream()
                .map(this::toMessage)
                .forEach(responseObserver::onNext);
        responseObserver.onCompleted();
    }

    private @NotNull GIS.Municipality toMessage(@NotNull Municipality municipality) {
        return GIS.Municipality.newBuilder()
                .setNationalCode(ConversionUtil.toMessage(municipality.getNationalCode()))
                .setCenter(ConversionUtil.toMessage(municipality.getCenter()))
                .setName(ConversionUtil.toMessage(municipality))
                .build();
    }
}
