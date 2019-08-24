package net.pkhapps.idispatch.core.domain.common;

import org.jetbrains.annotations.NotNull;

/**
 * Factory for creating domain object IDs.
 */
@FunctionalInterface
public interface IdFactory<ID> {

    /**
     * Creates and returns a new unique ID. This method is guaranteed to never return the same ID.
     */
    @NotNull ID createId();
}
