package net.pkhapps.idispatch.domain.support;

import javax.annotation.Nonnull;
import java.io.Serializable;

/**
 * Interface implemented by domain objects that can be identified by an ID.
 *
 * @param <ID> the ID type of the domain object.
 */
public interface IdentifiableDomainObject<ID extends DomainObjectId> extends Serializable {

    /**
     * Returns the ID of this domain object.
     */
    @Nonnull
    ID id();
}
