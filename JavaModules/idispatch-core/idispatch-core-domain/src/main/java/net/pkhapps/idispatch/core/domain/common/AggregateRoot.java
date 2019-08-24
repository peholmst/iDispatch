package net.pkhapps.idispatch.core.domain.common;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Base class for aggregate roots.
 */
public abstract class AggregateRoot<ID> extends Entity<ID> {

    private List<DomainEvent> domainEvents;

    protected AggregateRoot(@NotNull ID id) {
        super(id);
    }

    protected AggregateRoot(@NotNull IdFactory<ID> idFactory) {
        super(idFactory);
    }

    /**
     * Registers the given domain event for publication once the aggregate root is saved by a repository.
     *
     * @param event the event to publish.
     * @see #domainEvents()
     */
    protected final void publishEvent(@NotNull DomainEvent event) {
        if (domainEvents == null) {
            domainEvents = new ArrayList<>();
        }
        domainEvents.add(event);
    }

    /**
     * Returns the domain events currently registered for publication. The events will be returned in the same order
     * they were registered.
     *
     * @see #publishEvent(DomainEvent)
     */
    protected final @NotNull Iterable<DomainEvent> domainEvents() {
        return domainEvents == null ? Collections.emptyList() : List.copyOf(domainEvents);
    }

    /**
     * Clears all the domain events that have been registered. This method is typically called by the repository
     * after the events have been published.
     */
    protected final void clearDomainEvents() {
        domainEvents.clear();
    }
}
