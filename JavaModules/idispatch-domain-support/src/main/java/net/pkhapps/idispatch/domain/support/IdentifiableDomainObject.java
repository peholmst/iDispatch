package net.pkhapps.idispatch.domain.support;

import javax.annotation.Nonnull;
import java.io.Serializable;

public interface IdentifiableDomainObject<ID extends DomainObjectId> extends Serializable {

    @Nonnull
    ID id();
}
