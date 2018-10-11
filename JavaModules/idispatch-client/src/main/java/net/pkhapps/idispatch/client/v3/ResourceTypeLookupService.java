package net.pkhapps.idispatch.client.v3;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

import javax.annotation.Nonnull;
import java.util.List;

/**
 * Client interface for looking up {@link ResourceType}s from the server through REST.
 */
public interface ResourceTypeLookupService {

    String RELATIVE_SERVICE_URL = "v3/lookup/resource-type";

    @GET(RELATIVE_SERVICE_URL + "/active")
    @Nonnull
    Call<List<ResourceType>> findActiveResourceTypes();

    @GET(RELATIVE_SERVICE_URL + "/{id}")
    @Nonnull
    Call<ResourceType> findByResourceTypeId(@Path("id") @Nonnull ResourceTypeId id);
}
