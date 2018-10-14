package net.pkhapps.idispatch.domain.support;

import javax.annotation.Nonnull;

/**
 * Domain event interface for events published by an {@link AggregateRoot}.
 *
 * @param <ID> the ID type of the aggregate that published the event.
 */
public interface AggregateDomainEvent<ID extends DomainObjectId> extends DomainEvent {

    /**
     * Returns the ID of the aggregate root that published the event.
     */
    @Nonnull
    ID publishedBy();
}
