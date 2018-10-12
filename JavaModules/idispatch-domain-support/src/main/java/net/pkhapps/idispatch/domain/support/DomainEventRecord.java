package net.pkhapps.idispatch.domain.support;

import javax.annotation.Nonnull;

public interface DomainEventRecord<E extends DomainEvent> {

    @Nonnull
    DomainObjectId id();

    @Nonnull
    E event();
}
