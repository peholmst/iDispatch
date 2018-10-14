package net.pkhapps.idispatch.domain.support;

import javax.annotation.Nonnull;
import java.io.Serializable;
import java.time.Instant;

/**
 * Interface defining a domain event. A domain event is something that happens inside the domain model that other
 * domains (or aggregates within the same domain) may be interested in.
 *
 * @see DomainEventPublisher
 * @see DomainEventStore
 * @see AggregateDomainEvent
 * @see AggregateRoot#registerDomainEvent(DomainEvent)
 */
public interface DomainEvent extends Serializable {

    /**
     * Returns the date and time on which the domain event occurred.
     */
    @Nonnull
    Instant occurredOn();
}
