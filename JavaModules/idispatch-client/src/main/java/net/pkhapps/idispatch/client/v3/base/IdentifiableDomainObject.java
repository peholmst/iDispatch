package net.pkhapps.idispatch.client.v3.base;

import javax.annotation.Nonnull;
import java.io.Serializable;

/**
 * Interface implemented by domain objects that can be identified by some unique ID.
 */
public interface IdentifiableDomainObject<ID extends DomainObjectId> extends Serializable {

    @Nonnull
    ID id();
}
