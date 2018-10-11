package net.pkhapps.idispatch.client.v3;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

import javax.annotation.Nonnull;
import java.util.List;

/**
 * Client interface for looking up {@link Resource}s from the server through REST.
 */
public interface ResourceLookupService {

    String RELATIVE_SERVICE_URL = "v3/lookup/resource";

    @GET(RELATIVE_SERVICE_URL + "/active")
    @Nonnull
    Call<List<Resource>> findActiveResources();

    @GET(RELATIVE_SERVICE_URL + "/{id}")
    @Nonnull
    Call<Resource> findByResourceId(@Path("id") @Nonnull ResourceId id);
}
