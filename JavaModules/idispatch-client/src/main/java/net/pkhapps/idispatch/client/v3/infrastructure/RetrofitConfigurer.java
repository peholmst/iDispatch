package net.pkhapps.idispatch.client.v3.infrastructure;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import javax.annotation.Nonnull;
import java.util.Objects;


/**
 * Utility class for configuring a {@link Retrofit}-instance through its {@link Retrofit.Builder}. This configurer
 * will register all the converter factories needed by the iDispatch Client module, among other things.
 */
@SuppressWarnings("WeakerAccess")
public class RetrofitConfigurer {

    private final GsonConfigurer gsonConfigurer;

    public RetrofitConfigurer(@Nonnull GsonConfigurer gsonConfigurer) {
        this.gsonConfigurer = Objects.requireNonNull(gsonConfigurer, "gsonConfigurer must not be null");
    }

    public RetrofitConfigurer() {
        this(new GsonConfigurer());
    }

    @Nonnull
    protected Gson createGson() {
        return gsonConfigurer.configure(new GsonBuilder()).create();
    }

    @Nonnull
    public Retrofit.Builder configure(@Nonnull Retrofit.Builder retrofitBuilder) {
        Objects.requireNonNull(retrofitBuilder, "retrofitBuilder must not be null");
        return retrofitBuilder
                .addConverterFactory(new DomainObjectIdConverterFactory())
                .addConverterFactory(GsonConverterFactory.create(createGson()));
    }
}
