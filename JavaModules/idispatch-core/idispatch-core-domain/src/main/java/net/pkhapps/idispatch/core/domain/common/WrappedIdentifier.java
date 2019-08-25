package net.pkhapps.idispatch.core.domain.common;

import org.jetbrains.annotations.NotNull;

import java.lang.reflect.InvocationTargetException;
import java.util.Objects;

import static java.util.Objects.requireNonNull;

/**
 * TODO Document me!
 */
public abstract class WrappedIdentifier implements ValueObject {

    private final Object identifier;

    /**
     * @param identifier
     */
    public WrappedIdentifier(@NotNull Object identifier) {
        this.identifier = requireNonNull(identifier);
    }

    /**
     * @param idClass
     * @param id
     * @param <ID>
     * @return
     */
    public static <ID extends WrappedIdentifier> @NotNull ID wrap(@NotNull Class<ID> idClass, @NotNull Object id) {
        requireNonNull(idClass);
        requireNonNull(id);
        try {
            return idClass.getConstructor(Object.class).newInstance(id);
        } catch (NoSuchMethodException | IllegalAccessException | InstantiationException
                | InvocationTargetException ex) {
            throw new RuntimeException("Error wrapping ID", ex);
        }
    }

    /**
     * @return
     */
    public @NotNull Object unwrap() {
        return identifier;
    }

    /**
     * @param wrappedClass
     * @param <T>
     * @return
     */
    public <T> @NotNull T unwrap(@NotNull Class<T> wrappedClass) {
        return wrappedClass.cast(identifier);
    }

    @Override
    public String toString() {
        return identifier.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        WrappedIdentifier that = (WrappedIdentifier) o;
        return identifier.equals(that.identifier);
    }

    @Override
    public int hashCode() {
        return Objects.hash(identifier);
    }
}
