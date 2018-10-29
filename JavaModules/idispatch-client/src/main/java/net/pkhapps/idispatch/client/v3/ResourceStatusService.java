package net.pkhapps.idispatch.client.v3;

import net.pkhapps.idispatch.client.v3.geo.GeographicLocation;
import retrofit2.Call;

public interface ResourceStatusService {

    Call<ResourceStatus> currentStatus(ResourceId resourceId);

    Call<ResourceStatus> changeState(ResourceId resourceId, ResourceStateId newState);

    Call<ResourceStatus> changeLocation(ResourceId resourceId, GeographicLocation newLocation);
}
