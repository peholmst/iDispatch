package net.pkhapps.idispatch.client.v3.type;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

import javax.annotation.Nonnull;
import java.util.List;

/**
 * Client interface for looking up {@link Station}s from the server through REST.s
 */
public interface StationLookupService {

    String RELATIVE_SERVICE_URL = "v3/lookup/station";

    /**
     * Find all active stations.
     */
    @GET(RELATIVE_SERVICE_URL + "/active")
    @Nonnull
    Call<List<Station>> findActiveStations();

    /**
     * Find the station with the specified ID.
     */
    @GET(RELATIVE_SERVICE_URL + "/{id}")
    @Nonnull
    Call<Station> findByStationId(@Path("id") @Nonnull StationId id);
}
