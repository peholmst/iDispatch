package net.pkhapps.idispatch.client.v3.type;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

import javax.annotation.Nonnull;
import java.util.List;

/**
 * Client interface for looking up {@link Municipality Municipalities} from the server through REST.
 */
public interface MunicipalityLookupService {

    String RELATIVE_SERVICE_URL = "v3/lookup/municipality";

    @GET(RELATIVE_SERVICE_URL + "/active")
    @Nonnull
    Call<List<Municipality>> findActiveMunicipalities();

    @GET(RELATIVE_SERVICE_URL + "/{id}")
    @Nonnull
    Call<Municipality> findByMunicipalityId(@Path("id") @Nonnull MunicipalityId id);
}
