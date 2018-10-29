package net.pkhapps.idispatch.domain.support;

import javax.annotation.Nonnull;
import java.io.Serializable;

/**
 * TODO Document me!
 */
public class DomainEventRecordId extends DomainObjectId {

    /**
     * Creates a new domain object ID.
     *
     * @param id the serializable ID to wrap (such as a string or an integer).
     */
    public DomainEventRecordId(@Nonnull Serializable id) {
        super(id);
    }
}
