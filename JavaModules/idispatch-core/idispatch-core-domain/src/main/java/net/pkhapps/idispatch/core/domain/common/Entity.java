package net.pkhapps.idispatch.core.domain.common;

import org.jetbrains.annotations.NotNull;

import static java.util.Objects.requireNonNull;

/**
 * Base class for entities.
 */
public abstract class Entity<ID> implements IdentifiableDomainObject<ID> {

    private final ID id;

    Entity(@NotNull ID id) {
        this.id = requireNonNull(id);
    }

    Entity(@NotNull IdFactory<ID> idFactory) {
        this(requireNonNull(idFactory).createId());
    }

    @Override
    public final @NotNull ID id() {
        return id;
    }

    @Override
    public String toString() {
        return String.format("%s{%s}", getClass().getSimpleName(), id);
    }

    // TODO Equals & hashCode
}
