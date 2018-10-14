package net.pkhapps.idispatch.domain.support;

import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.NotThreadSafe;
import java.util.Objects;

/**
 * Base class for entities. An entity is a domain object that can be identified by a unique ID. Two entities of
 * the same class and with the same ID are considered the same entity.
 *
 * @param <ID> the ID type of the entity.
 */
@EqualsAndHashCode(of = "id")
@ToString(of = "id")
@NotThreadSafe
public abstract class Entity<ID extends DomainObjectId> implements IdentifiableDomainObject<ID>, Cloneable {

    private ID id;

    @UsedByPersistenceFramework
    protected Entity() {
    }

    /**
     * Creates a new {@code Entity} with the given ID.
     *
     * @param id the ID to use.
     */
    protected Entity(@Nonnull ID id) {
        this.id = Objects.requireNonNull(id, "id must not be null");
    }

    @Nonnull
    @Override
    public ID id() {
        return id;
    }

    @Override
    @SuppressWarnings("unchecked")
    public Entity<ID> clone() {
        try {
            return (Entity<ID>) super.clone();
        } catch (CloneNotSupportedException ex) {
            throw new RuntimeException("Could not clone entity", ex);
        }
    }
}
