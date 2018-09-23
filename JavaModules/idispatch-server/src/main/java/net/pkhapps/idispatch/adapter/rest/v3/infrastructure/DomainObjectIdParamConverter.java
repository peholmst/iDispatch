package net.pkhapps.idispatch.adapter.rest.v3.infrastructure;

import net.pkhapps.idispatch.client.v3.base.DomainObjectId;

import javax.annotation.Nonnull;
import javax.ws.rs.ext.ParamConverter;
import java.util.Objects;
import java.util.function.Function;

/**
 * JAX-RS {@link ParamConverter} for converting between strings and {@link DomainObjectId}s.
 */
@SuppressWarnings("WeakerAccess")
public class DomainObjectIdParamConverter<T extends DomainObjectId> implements ParamConverter<T> {

    private final Function<Long, T> factory;

    /**
     * Creates a new param converter that uses the given factory to create object instances.
     *
     * @param factory the factory to use.
     */
    public DomainObjectIdParamConverter(@Nonnull Function<Long, T> factory) {
        this.factory = Objects.requireNonNull(factory, "factory must not be null");
    }

    /**
     * Creates a new param converter that uses a constructor accepting a single {@code long} argument to
     * create object instances.
     *
     * @param domainObjectIdClass the concrete {@link DomainObjectId} class.
     * @throws IllegalArgumentException if the class does not have a suitable constructor.
     */
    public DomainObjectIdParamConverter(@Nonnull Class<T> domainObjectIdClass) {
        this(createFactoryFromConstructor(domainObjectIdClass));
    }

    private static <T extends DomainObjectId> Function<Long, T> createFactoryFromConstructor(
            @Nonnull Class<T> domainObjectIdClass) {
        Objects.requireNonNull(domainObjectIdClass, "domainObjectIdClass must not be null");
        try {
            var constructor = domainObjectIdClass.getConstructor(Long.class);
            return id -> {
                try {
                    return constructor.newInstance(id);
                } catch (Exception ex) {
                    throw new IllegalArgumentException("Could not create new instance of " + domainObjectIdClass, ex);
                }
            };
        } catch (NoSuchMethodException ex) {
            throw new IllegalArgumentException(domainObjectIdClass + " has not a suitable constructor");
        }
    }

    @Override
    public T fromString(String value) {
        return factory.apply(Long.valueOf(value));
    }

    @Override
    public String toString(T value) {
        return String.valueOf(value.toLong());
    }
}
