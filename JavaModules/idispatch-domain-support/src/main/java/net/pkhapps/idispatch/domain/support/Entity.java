package net.pkhapps.idispatch.domain.support;

import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.annotation.Nonnull;
import java.util.Objects;

/**
 * TODO Document me!
 *
 * @param <ID>
 */
@EqualsAndHashCode(of = "id")
@ToString(of = "id")
public abstract class Entity<ID extends DomainObjectId> implements IdentifiableDomainObject<ID>, Cloneable {

    private ID id;

    @UsedByPersistenceFramework
    protected Entity() {
    }

    /**
     * @param id
     */
    protected Entity(@Nonnull ID id) {
        this.id = Objects.requireNonNull(id, "id must not be null");
    }

    @Nonnull
    @Override
    public ID id() {
        return id;
    }
}
