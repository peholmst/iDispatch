package net.pkhapps.idispatch.domain.base;

import org.springframework.lang.NonNull;

import java.io.Serializable;
import java.util.Objects;

/**
 * Base class for aggregate root ID types (essentially a wrapper around a long integer).
 * <p/>
 * You will also need to implement support for your custom ID types into
 * your JPA implementation since {@link javax.persistence.AttributeConverter}s don't work with ID types (according
 * to the API spec). This project has base classes for implementing custom Hibernate types for your ID types.
 *
 * @see net.pkhapps.idispatch.domain.base.hibernate.AbstractAggregateRootIdCustomType
 * @see net.pkhapps.idispatch.domain.base.hibernate.AbstractAggregateRootIdTypeDescriptor
 */
public abstract class AbstractAggregateRootId implements Serializable {

    private final Long id;

    /**
     * Constructor that accepts a long integer as parameter. Subclasses must include this constructor since the
     * Hibernate custom type will use it to create new instances.
     */
    public AbstractAggregateRootId(@NonNull Long id) {
        this.id = Objects.requireNonNull(id);
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
