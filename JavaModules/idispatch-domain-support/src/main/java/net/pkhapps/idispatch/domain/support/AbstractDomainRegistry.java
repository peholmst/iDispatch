package net.pkhapps.idispatch.domain.support;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.ThreadSafe;
import java.time.Clock;
import java.util.Objects;

/**
 * TODO Document me!
 */
@ThreadSafe
public abstract class AbstractDomainRegistry {

    private final Clock clock;
    private final DomainEventPublisher domainEventPublisher;
    private final DomainEventStore domainEventStore;

    protected AbstractDomainRegistry(@Nonnull Clock clock,
                                     @Nonnull DomainEventPublisher domainEventPublisher,
                                     @Nonnull DomainEventStore domainEventStore) {
        this.clock = Objects.requireNonNull(clock, "clock must not be null");
        this.domainEventPublisher = Objects.requireNonNull(domainEventPublisher, "domainEventPublisher must not be null");
        this.domainEventStore = Objects.requireNonNull(domainEventStore, "domainEventStore must not be null");
    }

    @Nonnull
    public Clock clock() {
        return clock;
    }

    @Nonnull
    public DomainEventPublisher domainEventPublisher() {
        return domainEventPublisher;
    }

    @Nonnull
    public DomainEventStore domainEventStore() {
        return domainEventStore;
    }
}
