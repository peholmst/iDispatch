package net.pkhapps.idispatch.shared.domain.base;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.data.util.ProxyUtils;

import javax.persistence.*;
import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.util.Objects;

/**
 * Base class for entities.
 *
 * @param <ID>       the real ID type to store in the database.
 * @param <DomainId> the {@link DomainObjectId} type to use in the API.
 */
@MappedSuperclass
public abstract class BaseEntity<ID extends Serializable, DomainId extends DomainObjectId<ID>>
        implements IdentifiableDomainObject<DomainId> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, updatable = false)
    private ID id;

    @SuppressWarnings("unused")
    @Version
    @Column(name = "version", nullable = false)
    private Long version;

    @Transient
    private DomainId domainId;

    /**
     * Default constructor for new instances.
     */
    public BaseEntity() {
    }

    /**
     * Copy constructor.
     *
     * @param source the entity to copy from.
     */
    public BaseEntity(@NotNull BaseEntity<ID, DomainId> source) {
        Objects.requireNonNull(source, "source must not be null");
        this.id = source.id;
        this.domainId = source.domainId;
        this.version = source.version;
    }

    @NotNull
    @Override
    public final DomainId id() {
        if (domainId == null && id != null) {
            domainId = DomainObjectId.wrap(domainIdType(), id);
        }
        if (domainId == null) {
            throw new IllegalStateException("Entity has no ID yet");
        }
        return domainId;
    }

    @Override
    public boolean hasId() {
        return id != null;
    }

    private void setId(@Nullable DomainId id) {
        this.domainId = id;
        this.id = DomainObjectId.unwrap(id);
    }

    /**
     * Returns the optimistic locking version or {@code null} if the entity has not been persisted yet.
     */
    @Nullable
    protected Long version() {
        return version;
    }

    /**
     * Returns the {@link DomainObjectId} subclass used for {@link #id() entity IDs}. The default implementation will
     * use introspection to determine the type. If it does not work, you need to override this method.
     */
    @NotNull
    @SuppressWarnings("unchecked")
    protected Class<DomainId> domainIdType() {
        return (Class<DomainId>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[1];
    }

    @SuppressWarnings("EqualsWhichDoesntCheckParameterClass") // We do this with a Spring function
    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj == null || !getClass().equals(ProxyUtils.getUserClass(obj))) {
            return false;
        }

        var other = (BaseEntity<?, ?>) obj;
        return id != null && id.equals(other.id);
    }

    @Override
    public int hashCode() {
        return id == null ? super.hashCode() : id.hashCode();
    }

    @Override
    public String toString() {
        return String.format("%s@%s", getClass().getSimpleName(), id);
    }
}
