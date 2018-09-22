package net.pkhapps.idispatch.client.v3.util.gson;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import net.pkhapps.idispatch.client.v3.util.MultilingualString;

import java.lang.reflect.Type;
import java.util.Locale;
import java.util.Map;

/**
 * Type adapter ({@link JsonSerializer} and {@link JsonDeserializer}) for {@link MultilingualString}.
 */
public class MultilingualStringTypeAdapter implements JsonSerializer<MultilingualString>,
        JsonDeserializer<MultilingualString> {

    @Override
    public MultilingualString deserialize(JsonElement jsonElement, Type type,
                                          JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        return new MultilingualString(jsonDeserializationContext.deserialize(jsonElement,
                new TypeToken<Map<Locale, String>>() {
                }.getType()));
    }

    @Override
    public JsonElement serialize(MultilingualString multilingualString, Type type,
                                 JsonSerializationContext jsonSerializationContext) {
        return jsonSerializationContext.serialize(multilingualString.toMap());
    }
}
