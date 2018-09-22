package net.pkhapps.idispatch.client.v3.util.gson;

import com.google.gson.*;
import net.pkhapps.idispatch.client.v3.util.PhoneNumber;

import java.lang.reflect.Type;

/**
 * Type adapter ({@link JsonSerializer} and {@link JsonDeserializer}) for {@link PhoneNumber}.
 */
public class PhoneNumberJsonTypeAdapter implements JsonSerializer<PhoneNumber>, JsonDeserializer<PhoneNumber> {

    @Override
    public JsonElement serialize(PhoneNumber phoneNumber, Type type,
                                 JsonSerializationContext jsonSerializationContext) {
        return new JsonPrimitive(phoneNumber.toE164());
    }

    @Override
    public PhoneNumber deserialize(JsonElement jsonElement, Type type,
                                   JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        return new PhoneNumber(jsonElement.getAsString());
    }
}
