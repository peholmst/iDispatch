package net.pkhapps.idispatch.cad.infrastructure.jpa.event;

import net.pkhapps.idispatch.application.support.infrastructure.tx.UnitOfWorkManager;
import net.pkhapps.idispatch.domain.support.DomainEvent;
import net.pkhapps.idispatch.domain.support.DomainEventLog;
import net.pkhapps.idispatch.domain.support.DomainEventStore;

import javax.annotation.Nonnull;
import javax.persistence.EntityManager;

public class JpaDomainEventStore implements DomainEventStore {

    private final UnitOfWorkManager unitOfWorkManager;

    public JpaDomainEventStore(UnitOfWorkManager unitOfWorkManager) {
        this.unitOfWorkManager = unitOfWorkManager;
    }

    @Nonnull
    private EntityManager entityManager() {
        return unitOfWorkManager.requireExisting().unwrap(EntityManager.class);
    }

    @Override
    public void add(@Nonnull DomainEvent domainEvent) {
        entityManager().persist(new DomainEventRecordEntity(domainEvent));
    }

    @Nonnull
    @Override
    public DomainEventLog<? extends DomainEvent> currentLog() {
        throw new UnsupportedOperationException("Implement me!");
    }

    @Nonnull
    @Override
    public <E extends DomainEvent> DomainEventLog<E> currentLog(@Nonnull Class<E> domainEventClass) {
        throw new UnsupportedOperationException("Implement me!");
    }

    @Nonnull
    @Override
    public DomainEventLog<? extends DomainEvent> archivedLog(long offset) {
        throw new UnsupportedOperationException("Implement me!");
    }

    @Nonnull
    @Override
    public <E extends DomainEvent> DomainEventLog<E> archivedLog(@Nonnull Class<E> domainEventClass, long offset) {
        throw new UnsupportedOperationException("Implement me!");
    }
}
