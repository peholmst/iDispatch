package net.pkhapps.idispatch.shared.domain.base;

import org.jetbrains.annotations.Nullable;

/**
 * Interface for domain objects that can be uniquely identified.
 *
 * @param <ID> the {@link DomainObjectId} type.
 */
public interface IdentifiableDomainObject<ID extends DomainObjectId<?>> extends DomainObject {

    /**
     * Returns the ID of this domain object if one has been assigned.
     */
    @Nullable
    ID getId();
}
