package net.pkhapps.idispatch.shared.domain.base;

import org.jetbrains.annotations.NotNull;

/**
 * Interface for domain objects that can be uniquely identified.
 *
 * @param <ID> the {@link DomainObjectId} type.
 */
public interface IdentifiableDomainObject<ID extends DomainObjectId<?>> extends DomainObject {

    /**
     * Returns the ID of this domain object.
     *
     * @throws IllegalStateException if the domain object has no ID yet.
     * @see #hasId()
     */
    @NotNull
    ID id();

    /**
     * Returns whether this domain object has an {@link #id() ID} already.
     */
    boolean hasId();
}
