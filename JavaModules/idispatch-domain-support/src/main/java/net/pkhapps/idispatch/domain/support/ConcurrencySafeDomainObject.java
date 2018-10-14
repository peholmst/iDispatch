package net.pkhapps.idispatch.domain.support;

import java.io.Serializable;

/**
 * Interface implemented by domain objects that use optimistic locking to prevent concurrent modification.
 */
public interface ConcurrencySafeDomainObject extends Serializable {

    /**
     * Returns the version number of this domain object.
     */
    long version();
}
