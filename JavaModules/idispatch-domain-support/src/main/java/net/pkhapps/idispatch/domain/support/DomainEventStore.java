package net.pkhapps.idispatch.domain.support;

import javax.annotation.Nonnull;

/**
 * TODO Document me!
 */
public interface DomainEventStore {

    void add(@Nonnull DomainEvent domainEvent);

    @Nonnull
    DomainEventLog<? extends DomainEvent> currentLog();

    @Nonnull
    <E extends DomainEvent> DomainEventLog<E> currentLog(@Nonnull Class<E> domainEventClass);

    @Nonnull
    DomainEventLog<? extends DomainEvent> archivedLog(long offset);

    @Nonnull
    <E extends DomainEvent> DomainEventLog<E> archivedLog(@Nonnull Class<E> domainEventClass, long offset);
}
