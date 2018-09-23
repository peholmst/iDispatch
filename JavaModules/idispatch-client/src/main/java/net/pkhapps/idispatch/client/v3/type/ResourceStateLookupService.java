package net.pkhapps.idispatch.client.v3.type;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

import javax.annotation.Nonnull;
import java.util.List;

/**
 * Client interface for looking up {@link ResourceState}s from the server through REST.
 */
public interface ResourceStateLookupService {

    String RELATIVE_SERVICE_URL = "v3/lookup/resource-state";

    @GET(RELATIVE_SERVICE_URL + "/active")
    @Nonnull
    Call<List<ResourceState>> findActiveResourceStates();

    @GET(RELATIVE_SERVICE_URL + "/{id}")
    @Nonnull
    Call<ResourceState> findByResourceStateId(@Path("id") @Nonnull ResourceStateId id);
}
