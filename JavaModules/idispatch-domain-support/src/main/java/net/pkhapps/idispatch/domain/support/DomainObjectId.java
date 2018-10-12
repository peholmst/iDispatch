package net.pkhapps.idispatch.domain.support;

import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.Immutable;
import java.io.Serializable;
import java.util.Objects;

/**
 * Base class for value objects that represent the ID of some domain object.
 */
@Immutable
@EqualsAndHashCode
@ToString
public abstract class DomainObjectId implements Serializable {

    private final Serializable id;

    public DomainObjectId(@Nonnull Serializable id) {
        this.id = Objects.requireNonNull(id, "id must not be null");
    }

    @Nonnull
    public Serializable unwrap() {
        return id;
    }
}
