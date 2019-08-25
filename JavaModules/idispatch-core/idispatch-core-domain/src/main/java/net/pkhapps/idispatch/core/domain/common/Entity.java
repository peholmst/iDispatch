package net.pkhapps.idispatch.core.domain.common;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

import static java.util.Objects.requireNonNull;

/**
 * Base class for entities.
 */
public abstract class Entity<ID> implements IdentifiableDomainObject<ID> {

    private final ID id;

    Entity(@NotNull ID id) {
        this.id = requireNonNull(id);
    }

    Entity(@NotNull Essence<ID> essence) {
        requireNonNull(essence);
        if (!essence.isValid()) {
            throw new IllegalArgumentException("Essence object is not valid");
        }
        id = essence.id;
    }

    @Override
    public final @NotNull ID id() {
        return id;
    }

    @Override
    public String toString() {
        return String.format("%s{%s}", getClass().getSimpleName(), id);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Entity<?> entity = (Entity<?>) o;
        return id.equals(entity.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    /**
     * Base class designed to be used by repositories to re-create entities using the essence pattern, without needing
     * to go through cumbersome constructors or invoking business methods.
     */
    public static abstract class Essence<ID> {

        private ID id;

        public Essence() {
        }

        public Essence(@NotNull Entity<ID> source) {
            requireNonNull(source);
            this.id = source.id;
        }

        public ID getId() {
            return id;
        }

        public void setId(ID id) {
            this.id = id;
        }

        /**
         * Checks that the essence object contains enough information to be able to create a valid entity.
         */
        protected boolean isValid() {
            return id != null;
        }
    }
}
