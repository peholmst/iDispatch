package net.pkhapps.idispatch.client.v3.base;

import javax.annotation.Nonnull;
import java.io.Serializable;
import java.util.Objects;

/**
 * Interface implemented by domain objects that are versioned.
 */
public interface VersionedDomainObject extends Serializable {

    long version();

    default boolean isNewerThan(@Nonnull VersionedDomainObject other) {
        Objects.requireNonNull(other, "other must not be null");
        return version() > other.version();
    }

    default boolean hasSameVersionAs(@Nonnull VersionedDomainObject other) {
        Objects.requireNonNull(other, "other must not be null");
        return version() == other.version();
    }

    default boolean isOlderThan(@Nonnull VersionedDomainObject other) {
        Objects.requireNonNull(other, "other must not be null");
        return version() < other.version();
    }
}
