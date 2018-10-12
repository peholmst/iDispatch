package net.pkhapps.idispatch.domain.support;

import javax.annotation.Nonnull;

public interface AggregateDomainEvent<ID extends DomainObjectId> extends DomainEvent {

    @Nonnull
    ID publishedBy();
}
