package net.pkhapps.idispatch.domain.support;

import lombok.extern.slf4j.Slf4j;

import javax.annotation.Nonnull;
import java.util.Objects;

@Slf4j
public abstract class AbstractRepository<ID extends DomainObjectId, T extends AggregateRoot<ID>>
        implements Repository<ID, T> {

    private final DomainEventStore domainEventStore;
    private final DomainEventPublisher domainEventPublisher;

    protected AbstractRepository(@Nonnull DomainEventStore domainEventStore,
                                 @Nonnull DomainEventPublisher domainEventPublisher) {
        this.domainEventStore = Objects.requireNonNull(domainEventStore, "domainEventStore must not be null");
        this.domainEventPublisher = Objects.requireNonNull(domainEventPublisher, "domainEventPublisher must not be null");
    }

    protected static boolean isNew(@Nonnull AggregateRoot<?> aggregateRoot) {
        Objects.requireNonNull(aggregateRoot, "aggregateRoot must not be null");
        return aggregateRoot.isNew();
    }

    protected static void setNew(@Nonnull AggregateRoot<?> aggregateRoot, boolean isNew) {
        Objects.requireNonNull(aggregateRoot, "aggregateRoot must not be null");
        aggregateRoot.setNew(isNew);
    }

    protected void storeDomainEvents(@Nonnull T aggregateRoot) {
        Objects.requireNonNull(aggregateRoot, "aggregateRoot must not be null");
        log.trace("Storing domain events of {}", aggregateRoot);
        aggregateRoot.domainEvents().forEach(domainEventStore::append);
    }

    protected void publishDomainEvents(@Nonnull T aggregateRoot) {
        Objects.requireNonNull(aggregateRoot, "aggregateRoot must not be null");
        log.trace("Publishing domain events of {}", aggregateRoot);
        try {
            aggregateRoot.domainEvents().forEach(domainEventPublisher::publish);
        } finally {
            log.trace("Clearing domain events for {}", aggregateRoot);
            aggregateRoot.clearDomainEvents();
        }
    }
}
