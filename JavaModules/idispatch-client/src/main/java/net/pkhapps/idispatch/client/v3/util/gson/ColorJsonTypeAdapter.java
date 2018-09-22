package net.pkhapps.idispatch.client.v3.util.gson;

import com.google.gson.*;
import net.pkhapps.idispatch.client.v3.util.Color;

import java.lang.reflect.Type;

/**
 * Type adapter ({@link JsonSerializer} and {@link JsonDeserializer}) for {@link Color}.
 */
public class ColorJsonTypeAdapter implements JsonSerializer<Color>, JsonDeserializer<Color> {

    @Override
    public JsonElement serialize(Color color, Type type, JsonSerializationContext jsonSerializationContext) {
        return new JsonPrimitive(color.toHexRGBString());
    }

    @Override
    public Color deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext)
            throws JsonParseException {
        return new Color(jsonElement.getAsString());
    }
}
