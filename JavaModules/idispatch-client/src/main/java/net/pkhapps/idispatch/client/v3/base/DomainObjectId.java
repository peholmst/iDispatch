package net.pkhapps.idispatch.client.v3.base;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.Immutable;
import java.io.Serializable;
import java.util.Objects;

/**
 * Base class for value objects that represent a unique ID of some domain object.
 */
@Immutable
public abstract class DomainObjectId implements Serializable {

    private final Long id;

    public DomainObjectId(@Nonnull Long id) {
        this.id = Objects.requireNonNull(id, "id must not be null");
    }

    @Nonnull
    public final Long toLong() {
        return id;
    }

    @Override
    public boolean equals(@Nullable Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DomainObjectId that = (DomainObjectId) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return String.format("%s[%x]", getClass().getSimpleName(), id);
    }
}
