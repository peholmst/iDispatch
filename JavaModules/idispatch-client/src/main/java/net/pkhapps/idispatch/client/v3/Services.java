package net.pkhapps.idispatch.client.v3;

import net.pkhapps.idispatch.client.v3.base.Principal;
import net.pkhapps.idispatch.client.v3.infrastructure.RetrofitConfigurer;
import net.pkhapps.idispatch.client.v3.type.ResourceStateLookupService;
import net.pkhapps.idispatch.client.v3.type.ResourceTypeLookupService;
import net.pkhapps.idispatch.client.v3.type.StationLookupService;
import net.pkhapps.idispatch.client.v3.util.LazyReference;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Response;
import retrofit2.Retrofit;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.ThreadSafe;
import javax.security.auth.Subject;
import java.io.IOException;
import java.security.AccessController;
import java.util.Objects;

/**
 * Class for easily accessing all the server REST resources through the Java service interfaces. Service instances will
 * be created lazily so there is no overhead in using this class even if the client is only using a fraction of the
 * services.
 */
@ThreadSafe
public class Services {

    private static final String API_KEY_HEADER = "iDispatch-API-Key";
    private static final String PRINCIPAL_ID_HEADER = "iDispatch-Principal";

    private final String apiKey;

    private final LazyReference<ResourceStateLookupService> resourceStateLookupService;
    private final LazyReference<ResourceTypeLookupService> resourceTypeLookupService;
    private final LazyReference<StationLookupService> stationLookupService;

    /**
     * Creates a new {@code Services} instance.
     *
     * @param baseUrl the base URL of the server REST endpoint.
     * @param apiKey  the API key to pass to the server with every request.
     */
    public Services(@Nonnull String baseUrl, @Nonnull String apiKey) {
        Objects.requireNonNull(baseUrl, "baseUrl must not be null");
        this.apiKey = Objects.requireNonNull(apiKey, "apiKey must not be null");
        var client = new OkHttpClient.Builder()
                .addInterceptor(this::addApiKeyToRequest)
                .addInterceptor(this::addSubjectToRequest)
                .build();
        var retrofit = new RetrofitConfigurer().configure(new Retrofit.Builder())
                .baseUrl(baseUrl)
                .client(client)
                .build();
        resourceStateLookupService = createClientService(retrofit, ResourceStateLookupService.class);
        resourceTypeLookupService = createClientService(retrofit, ResourceTypeLookupService.class);
        stationLookupService = createClientService(retrofit, StationLookupService.class);
    }

    private <T> LazyReference<T> createClientService(Retrofit retrofit, Class<T> serviceInterface) {
        return new LazyReference<>(() -> retrofit.create(serviceInterface));
    }

    private Response addApiKeyToRequest(Interceptor.Chain chain) throws IOException {
        var request = chain.request().newBuilder().addHeader(API_KEY_HEADER, apiKey).build();
        return chain.proceed(request);
    }

    private Response addSubjectToRequest(Interceptor.Chain chain) throws IOException {
        // TODO Not sure about this yet.
        var subject = Subject.getSubject(AccessController.getContext());
        if (subject != null) {
            var principals = subject.getPrincipals(Principal.class);
            if (principals.size() > 0) {
                var principalId = principals.iterator().next().id();
                var request = chain.request().newBuilder().addHeader(PRINCIPAL_ID_HEADER, principalId).build();
                return chain.proceed(request);
            }
        }
        return chain.proceed(chain.request());
    }

    @Nonnull
    ResourceStateLookupService resourceStateLookupService() {
        return resourceStateLookupService.get();
    }

    @Nonnull
    public ResourceTypeLookupService resourceTypeLookupService() {
        return resourceTypeLookupService.get();
    }

    @Nonnull
    public StationLookupService stationLookupService() {
        return stationLookupService.get();
    }
}
