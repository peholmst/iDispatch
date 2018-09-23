package net.pkhapps.idispatch.adapter.rest;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Objects;

/**
 * Base class for {@link javax.ws.rs.ext.ExceptionMapper}s that return a JSON body in addition to a status code.
 */
@SuppressWarnings("WeakerAccess")
public abstract class AbstractJsonExceptionMapper {

    private final Gson gson = new Gson();

    @Nonnull
    protected JsonObject toJsonObject(int status, @Nullable String message) {
        var json = new JsonObject();
        json.addProperty("status", status);
        if (message != null && !message.isEmpty()) {
            json.addProperty("message", message);
        }
        return json;
    }

    @Nonnull
    protected Response.ResponseBuilder toResponseBuilder(int status, @Nonnull JsonElement json) {
        Objects.requireNonNull(json, "json must not be null");
        return Response.status(status)
                .type(MediaType.APPLICATION_JSON_TYPE)
                .entity(gson.toJson(json));
    }

    @Nonnull
    protected Response toResponse(int status, @Nonnull JsonElement json) {
        return toResponseBuilder(status, json).build();
    }

    @Nonnull
    protected Response toResponse(int status, @Nullable String message) {
        return toResponse(status, toJsonObject(status, message));
    }
}
