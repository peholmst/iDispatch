package net.pkhapps.idispatch.shared.domain.base;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.Serializable;
import java.util.Objects;

/**
 * Base class for domain object IDs. These acts as wrapper around the real ID types.
 *
 * @param <ID> the wrapped ID type.
 * @see DomainObjectIdConverter
 */
public abstract class DomainObjectId<ID extends Serializable> implements ValueObject {

    private final ID id;

    /**
     * Creates a new {@code DomainObjectId}.
     *
     * @param id the ID to wrap.
     */
    @JsonCreator
    public DomainObjectId(@NotNull ID id) {
        this.id = Objects.requireNonNull(id, "id must not be null");
    }

    /**
     * Null-safe version of {@link #unwrap()}. If {@code domainObjectId} is {@code null}, this method returns
     * {@code null}. Otherwise it returns the result of {@link #unwrap()}.
     *
     * @param domainObjectId the domain object ID to unwrap.
     * @param <ID>           the type of the wrapped ID.
     * @return the wrapped ID or {@code null}.
     */
    @Nullable
    @Contract("null -> null")
    public static <ID extends Serializable> ID unwrap(@Nullable DomainObjectId<ID> domainObjectId) {
        return domainObjectId == null ? null : domainObjectId.unwrap();
    }

    /**
     * Creates a new instanceof a {@link DomainObjectId}-subclass that wraps the given {@code id}. If the {@code id}
     * is {@code null}, this method returns {@code null}.
     *
     * @param domainIdClass the type of {@link DomainObjectId} to create.
     * @param id            the ID to wrap.
     * @param <ID>          the type of the wrapped ID.
     * @param <DomainId>    the {@link DomainObjectId} subclass.
     * @return the {@link DomainObjectId} instance or {@code null}.
     */
    @Nullable
    @Contract("_, null -> null")
    public static <ID extends Serializable, DomainId extends DomainObjectId<ID>> DomainId wrap(
            @NotNull Class<DomainId> domainIdClass, @Nullable ID id) {
        try {
            return id == null ? null : domainIdClass.getConstructor(id.getClass()).newInstance(id);
        } catch (Exception ex) {
            throw new IllegalArgumentException("Could not wrap " + id + " inside a " + domainIdClass.getName());
        }
    }

    /**
     * Unwraps the domain object ID, returning the "real" ID.
     *
     * @return the wrapped ID.
     */
    @NotNull
    @JsonValue
    public ID unwrap() {
        return id;
    }

    @Override
    public String toString() {
        return id.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DomainObjectId<?> that = (DomainObjectId<?>) o;
        return id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
