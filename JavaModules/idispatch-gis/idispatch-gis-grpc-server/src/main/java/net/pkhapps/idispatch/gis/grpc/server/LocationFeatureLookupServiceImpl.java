package net.pkhapps.idispatch.gis.grpc.server;

import io.grpc.stub.StreamObserver;
import net.pkhapps.idispatch.gis.api.lookup.*;
import net.pkhapps.idispatch.gis.grpc.proto.GIS;
import net.pkhapps.idispatch.gis.grpc.proto.LocationFeatureLookupServiceGrpc;
import net.pkhapps.idispatch.gis.grpc.util.ConversionUtil;
import org.jetbrains.annotations.NotNull;

/**
 * TODO Document me
 */
class LocationFeatureLookupServiceImpl extends LocationFeatureLookupServiceGrpc.LocationFeatureLookupServiceImplBase {

    private final LocationFeatureLookupService locationFeatureLookupService;

    LocationFeatureLookupServiceImpl(@NotNull LocationFeatureLookupService locationFeatureLookupService) {
        this.locationFeatureLookupService = locationFeatureLookupService;
    }

    @Override
    public void findFeaturesCloseToPoint(GIS.Point request, StreamObserver<GIS.LocationFeature> responseObserver) {
        locationFeatureLookupService
                .findFeaturesCloseToPoint(ConversionUtil.fromMessage(request))
                .stream()
                .map(this::toMessage)
                .forEach(responseObserver::onNext);
        responseObserver.onCompleted();
    }

    @Override
    public void findFeaturesByName(GIS.LocationFeatureSearchRequest request, StreamObserver<GIS.LocationFeature> responseObserver) {
        var municipalityCode = request.hasMunicipality() ? ConversionUtil.fromMessage(request.getMunicipality()) : null;
        var name = request.getNameSearch().getSearchTerm();
        var matchStrategy = NameMatchStrategy.valueOf(request.getNameSearch().getMatchStrategy().name());
        var number = request.hasNumber() ? request.getNumber().getValue() : null;
        locationFeatureLookupService
                .findFeaturesByName(municipalityCode, name, matchStrategy, number)
                .stream()
                .map(this::toMessage)
                .forEach(responseObserver::onNext);
        responseObserver.onCompleted();
    }

    private @NotNull GIS.LocationFeature toMessage(@NotNull LocationFeature<?> feature) {
        var builder = GIS.LocationFeature.newBuilder()
                .setLocationAccuracy(feature.getLocationAccuracy().code());
        feature.getMunicipality().map(ConversionUtil::toMessage).ifPresent(builder::setMunicipality);
        feature.getValidFrom().map(ConversionUtil::toMessage).ifPresent(builder::setValidFrom);
        feature.getValidTo().map(ConversionUtil::toMessage).ifPresent(builder::setValidTo);
        if (feature instanceof AddressPoint) {
            var ap = (AddressPoint) feature;
            var apBuilder = GIS.AddressPoint.newBuilder()
                    .setName(ConversionUtil.toMessage(ap))
                    .setAddressPointClass(ap.getAddressPointClass().code())
                    .setLocation(ConversionUtil.toMessage(ap.getLocation()));
            ap.getNumber().map(ConversionUtil::wrap).ifPresent(apBuilder::setNumber);
            builder.setAddressPoint(apBuilder.build());
        } else if (feature instanceof RoadSegment) {
            var rs = (RoadSegment) feature;
            var rsBuilder = GIS.RoadSegment.newBuilder()
                    .setName(ConversionUtil.toMessage(rs))
                    .setRoadClass(rs.getRoadClass().code())
                    .setElevation(rs.getElevation().code())
                    .setSurface(rs.getSurface().code())
                    .setDirection(rs.getDirection().code())
                    .setLocation(ConversionUtil.toMessage(rs.getLocation()));
            rs.getRoadNumber().map(ConversionUtil::wrap).ifPresent(rsBuilder::setRoadNumber);
            rs.getRoadPartNumber().map(ConversionUtil::wrap).ifPresent(rsBuilder::setRoadPartNumber);
            rs.getAddressNumbersLeft().map(ConversionUtil::toMessage).ifPresent(rsBuilder::setAddressNumbersLeft);
            rs.getAddressNumbersRight().map(ConversionUtil::toMessage).ifPresent(rsBuilder::setAddressNumbersRight);
            builder.setRoadSegment(rsBuilder.build());
        }
        return builder.build();
    }
}
