package net.pkhapps.idispatch.core.client.resources;

import net.pkhapps.idispatch.core.client.organization.Organization;
import net.pkhapps.idispatch.core.client.organization.OrganizationId;
import net.pkhapps.idispatch.core.client.organization.OrganizationListingService;
import org.apache.commons.lang3.Validate;
import org.jetbrains.annotations.NotNull;

/**
 * TODO document me
 */
public final class Station {

    private final Organization organization;
    private final StationId stationId;

    Station(@NotNull net.pkhapps.idispatch.core.grpc.proto.resource.Station station,
            @NotNull OrganizationListingService organizationListingService) {
        Validate.isTrue(station.hasOrganization());
        Validate.isTrue(station.hasStationId());

        this.organization = organizationListingService.getById(new OrganizationId(station.getOrganization()));
        this.stationId = new StationId(station.getStationId());
    }
}
