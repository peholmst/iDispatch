package net.pkhapps.idispatch.client.v3.util.gson;

import com.google.gson.*;

import java.lang.reflect.Type;
import java.time.Instant;

/**
 * Type adapter ({@link JsonSerializer} and {@link JsonDeserializer}) that represents {@link Instant}s as ISO-8601
 * strings.
 */
public class InstantJsonTypeAdapter implements JsonSerializer<Instant>, JsonDeserializer<Instant> {

    @Override
    public Instant deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
            throws JsonParseException {
        return Instant.parse(json.getAsString());
    }

    @Override
    public JsonElement serialize(Instant src, Type typeOfSrc, JsonSerializationContext context) {
        return new JsonPrimitive(src.toString());
    }
}
