package net.pkhapps.idispatch.base.domain;

/**
 * Interface for domain objects that can be deactivated and reactivated.
 */
public interface Deactivatable extends DomainObject {

    /**
     * Returns whether this domain object is currently active.
     */
    boolean isActive();

    /**
     * Deactivates this domain object. If the object is already deactivated, nothing happens.
     */
    void deactivate();

    /**
     * Activates this domain object. If the object is already activated, nothing happens.
     */
    void activate();
}
