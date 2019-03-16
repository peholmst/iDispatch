package net.pkhapps.idispatch.base.domain;

/**
 * Interface for domain objects that can be uniquely identified.
 */
public interface IdentifiableDomainObject<ID> extends DomainObject {

    /**
     * Returns the ID of this domain object. Implementation may decide whether this method can return {@code null} or not.
     */
    ID getId();
}
