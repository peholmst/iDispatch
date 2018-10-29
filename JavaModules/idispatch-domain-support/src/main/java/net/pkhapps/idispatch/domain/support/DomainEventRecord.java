package net.pkhapps.idispatch.domain.support;

import javax.annotation.Nonnull;

/**
 * TODO Document me!
 */
public interface DomainEventRecord extends IdentifiableDomainObject<DomainEventRecordId> {

    @Nonnull
    DomainEvent event();
}
