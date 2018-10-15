package net.pkhapps.idispatch.cad.infrastructure.json.gson;

import com.google.gson.Gson;
import net.pkhapps.idispatch.cad.infrastructure.json.JsonConverter;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.ThreadSafe;
import java.util.Objects;

/**
 * Implementation of {@link JsonConverter} that uses {@link Gson}.
 */
@ThreadSafe
public class GsonConverter extends JsonConverter {

    private final Gson gson;

    /**
     * Creates a new {@code GsonConverter} instance. Remember to register all type adapters that will be needed when
     * creating the {@link Gson} instance.
     *
     * @param gson the {@link Gson} instance to use.
     */
    public GsonConverter(@Nonnull Gson gson) {
        this.gson = Objects.requireNonNull(gson, "gson must not be null");
    }

    @Nonnull
    @Override
    public String toJson(@Nonnull Object pojo) {
        return gson.toJson(pojo);
    }

    @Nonnull
    @Override
    public <T> T fromJson(@Nonnull Class<T> pojoType, @Nonnull String json) {
        return gson.fromJson(json, pojoType);
    }
}
