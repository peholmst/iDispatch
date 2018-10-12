package net.pkhapps.idispatch.domain.support;

import javax.annotation.Nonnull;

public interface DomainEventStore {

    void append(@Nonnull DomainEvent domainEvent);

    @Nonnull
    DomainEventLog<? extends DomainEvent> currentLog();

    @Nonnull
    <E extends DomainEvent> DomainEventLog<E> currentLog(@Nonnull Class<E> domainEventClass);

    @Nonnull
    DomainEventLog<? extends DomainEvent> archivedLog(long offset);

    @Nonnull
    <E extends DomainEvent> DomainEventLog<E> archivedLog(@Nonnull Class<E> domainEventClass, long offset);
}
