package net.pkhapps.idispatch.domain.base;

import org.springframework.lang.NonNull;

import java.io.Serializable;
import java.util.Objects;

/**
 * Base class for aggregate root ID types.
 */
public abstract class AbstractAggregateRootId implements Serializable {

    private final Long id;

    public AbstractAggregateRootId(@NonNull Long id) {
        this.id = Objects.requireNonNull(id);
    }

    public AbstractAggregateRootId(@NonNull String id) {
        this(Long.valueOf(id));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        AbstractAggregateRootId that = (AbstractAggregateRootId) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    /**
     * Returns the ID as a long integer.
     */
    public long toLong() {
        return id;
    }

    /**
     * Returns the ID as a string.
     */
    @Override
    public String toString() {
        return id.toString();
    }
}
