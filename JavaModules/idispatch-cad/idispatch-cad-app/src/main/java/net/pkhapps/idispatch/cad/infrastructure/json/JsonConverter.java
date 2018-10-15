package net.pkhapps.idispatch.cad.infrastructure.json;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.ThreadSafe;
import java.util.Objects;

/**
 * Abstract base class for a converter that converts between JSON and POJOs. The converter is thread safe once it has
 * been {@link #initialize(JsonConverter) initialized}. The converter should be able to convert all POJOs in the
 * system to JSON and back. It is up to the developer to make sure this is the case.
 */
@ThreadSafe
public abstract class JsonConverter {

    private static JsonConverter INSTANCE;

    /**
     * Returns the singleton {@code JsonConverter} instance.
     *
     * @throws IllegalStateException if the instance has not been {@link #initialize(JsonConverter) initialized}.
     */
    @Nonnull
    public static JsonConverter instance() {
        if (INSTANCE == null) {
            throw new IllegalStateException("The JsonConverter has not been initialized yet");
        }
        return INSTANCE;
    }

    /**
     * Initializes the singleton {@code JsonConverter} instance.
     *
     * @param jsonConverter the converter instance to use.
     */
    public static void initialize(@Nonnull JsonConverter jsonConverter) {
        INSTANCE = Objects.requireNonNull(jsonConverter, "jsonConverter must not be null");
    }

    /**
     * Converts the given POJO into a JSON string.
     *
     * @param pojo the POJO to convert.
     * @return the JSON string.
     * @throws RuntimeException if the POJO cannot be converted to JSON.
     */
    @Nonnull
    public abstract String toJson(@Nonnull Object pojo);

    /**
     * Converts the given JSON string into a POJO of the given type.
     *
     * @param pojoType the POJO type to convert to.
     * @param json     the JSON string to convert.
     * @param <T>      the POJO type.
     * @return the POJO.
     * @throws RuntimeException if the JSON string cannot be converted to a POJO.
     */
    @Nonnull
    public abstract <T> T fromJson(@Nonnull Class<T> pojoType, @Nonnull String json);
}
