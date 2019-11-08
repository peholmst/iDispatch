package net.pkhapps.idispatch.gis.grpc.server;

import io.grpc.stub.StreamObserver;
import net.pkhapps.idispatch.gis.api.Locales;
import net.pkhapps.idispatch.gis.api.lookup.Municipality;
import net.pkhapps.idispatch.gis.api.lookup.MunicipalityLookupService;
import net.pkhapps.idispatch.gis.api.lookup.NameMatchStrategy;
import net.pkhapps.idispatch.gis.grpc.proto.GIS;
import net.pkhapps.idispatch.gis.grpc.proto.MunicipalityLookupServiceGrpc;
import org.jetbrains.annotations.NotNull;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;

/**
 * TODO Document me
 */
class MunicipalityLookupServiceImpl extends MunicipalityLookupServiceGrpc.MunicipalityLookupServiceImplBase {

    private final MunicipalityLookupService municipalityLookupService;
    private final GeometryFactory geometryFactory = new GeometryFactory();

    MunicipalityLookupServiceImpl(@NotNull MunicipalityLookupService municipalityLookupService) {
        this.municipalityLookupService = municipalityLookupService;
    }

    @Override
    public void findMunicipalityOfPoint(GIS.Point request, StreamObserver<GIS.Municipality> responseObserver) {
        var coordinate = new Coordinate(request.getCoordinate().getX(), request.getCoordinate().getY());
        var point = geometryFactory.createPoint(coordinate);
        point.setSRID(request.getSrid());
        this.municipalityLookupService
                .findMunicipalityOfPoint(point)
                .map(this::toMessage)
                .ifPresentOrElse(responseObserver::onNext, responseObserver::onCompleted);
    }

    @Override
    public void findByNationalCode(GIS.SearchRequest request, StreamObserver<GIS.Municipality> responseObserver) {
        municipalityLookupService
                .findByNationalCode(request.getSearchTerm())
                .map(this::toMessage)
                .ifPresentOrElse(responseObserver::onNext, responseObserver::onCompleted);
    }

    @Override
    public void findByName(GIS.SearchRequest request, StreamObserver<GIS.Municipality> responseObserver) {
        municipalityLookupService
                .findByName(request.getSearchTerm(), NameMatchStrategy.valueOf(request.getMatchStrategy().name()))
                .stream()
                .map(this::toMessage)
                .forEach(responseObserver::onNext);
    }

    private @NotNull GIS.Municipality toMessage(@NotNull Municipality municipality) {
        return GIS.Municipality.newBuilder()
                .setNationalCode(municipality.getNationalCode().getCode())
                .setCenter(GIS.Point.newBuilder()
                        .setCoordinate(GIS.Coordinate.newBuilder()
                                .setX(municipality.getCenter().getX())
                                .setY(municipality.getCenter().getY())
                                .build())
                        .setSrid(municipality.getCenter().getSRID())
                        .build())
                .setName(GIS.MultilingualString.newBuilder()
                        .setFin(municipality.getName(Locales.FINNISH).orElse(null))
                        .setSwe(municipality.getName(Locales.SWEDISH).orElse(null))
                        .setSme(municipality.getName(Locales.NORTHERN_SAMI).orElse(null))
                        .setSmn(municipality.getName(Locales.INARI_SAMI).orElse(null))
                        .setSms(municipality.getName(Locales.SKOLT_SAMI).orElse(null))
                        .build())
                .build();
    }
}
