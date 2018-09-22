package net.pkhapps.idispatch.client.v3.base;

import java.io.Serializable;

/**
 * Interface implemented by domain object that can be deactivated ("soft-deleted").
 */
public interface DeactivatableDomainObject extends Serializable {

    boolean active();

    default boolean inactive() {
        return !active();
    }
}
