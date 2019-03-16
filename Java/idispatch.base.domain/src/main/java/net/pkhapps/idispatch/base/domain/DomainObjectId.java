package net.pkhapps.idispatch.base.domain;

import org.springframework.lang.NonNull;

import java.util.Objects;

/**
 * Base class for value objects that identify some other domain object (by wrapping a long integer).
 */
public class DomainObjectId implements ValueObject {

    private final long wrappedId;

    public DomainObjectId(long wrappedId) {
        this.wrappedId = wrappedId;
    }

    @NonNull
    public Long toLong() {
        return wrappedId;
    }

    @Override
    public String toString() {
        return String.valueOf(wrappedId);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DomainObjectId that = (DomainObjectId) o;
        return wrappedId == that.wrappedId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(wrappedId);
    }
}
