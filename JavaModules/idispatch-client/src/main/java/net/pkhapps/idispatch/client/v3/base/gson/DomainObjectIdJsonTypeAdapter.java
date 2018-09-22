package net.pkhapps.idispatch.client.v3.base.gson;

import com.google.gson.*;
import net.pkhapps.idispatch.client.v3.base.DomainObjectId;

import javax.annotation.Nonnull;
import java.lang.reflect.Type;
import java.util.Objects;
import java.util.function.Function;

/**
 * Type adapter ({@link JsonSerializer} and {@link JsonDeserializer}) for subclasses of {@link DomainObjectId}.
 */
public class DomainObjectIdJsonTypeAdapter<T extends DomainObjectId> implements JsonSerializer<T>, JsonDeserializer<T> {

    private final Function<Long, T> factory;

    /**
     * @param factory a factory for creating new instances of the domain object ID class.
     */
    public DomainObjectIdJsonTypeAdapter(@Nonnull Function<Long, T> factory) {
        this.factory = Objects.requireNonNull(factory, "factory must not be null");
    }

    @Override
    public T deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        return factory.apply(jsonElement.getAsJsonPrimitive().getAsLong());
    }

    @Override
    public JsonElement serialize(T t, Type type, JsonSerializationContext jsonSerializationContext) {
        return new JsonPrimitive(t.toLong());
    }
}
