package net.pkhapps.idispatch.core.domain.common;

import org.jetbrains.annotations.NotNull;

/**
 * Interface for domain objects that can be uniquely identified.
 */
public interface IdentifiableDomainObject<ID> extends DomainObject {

    /**
     * Returns the ID of this domain object.
     */
    @NotNull ID id();
}
