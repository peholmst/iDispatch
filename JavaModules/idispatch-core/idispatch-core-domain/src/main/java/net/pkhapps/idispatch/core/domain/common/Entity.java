package net.pkhapps.idispatch.core.domain.common;

import org.jetbrains.annotations.NotNull;

import static java.util.Objects.requireNonNull;

/**
 * Base class for entities.
 */
public abstract class Entity<ID> implements IdentifiableDomainObject<ID> {

    private final ID id;

    public Entity(@NotNull ID id) {
        this.id = requireNonNull(id);
    }

    @Override
    public @NotNull ID id() {
        return id;
    }

    // TODO Equals & hashCode
}
