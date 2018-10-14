package net.pkhapps.idispatch.domain.support;

import java.io.Serializable;

/**
 * Interface implemented by domain objects that can be deactivated and reactivated ("soft delete").
 */
@SuppressWarnings("SpellCheckingInspection")
public interface DeactivatableDomainObject extends Serializable {

    /**
     * Returns whether this domain object is active.
     */
    boolean active();

    /**
     * Returns whether this domain object is inactive.
     */
    default boolean inactive() {
        return !active();
    }

    /**
     * Deactivates ("softly deletes") this domain object. If the domain object is already inactive, nothing happens.
     */
    void deactivate();

    /**
     * Activates ("restores") this domain object. If the domain object is already active, nothing happens.
     */
    void activate();
}
