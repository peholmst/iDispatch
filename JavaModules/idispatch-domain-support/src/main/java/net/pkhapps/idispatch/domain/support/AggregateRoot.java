package net.pkhapps.idispatch.domain.support;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.NotThreadSafe;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

/**
 * Base class for aggregate roots. An aggregate root is a special kind of {@link Entity} that acts as the root of an
 * aggregate (a collection of domain objects that belong together). Aggregates are retrieved and stored as a whole and
 * the aggregate root is responsible for ensuring the integrity of the aggregate at all times.
 *
 * @param <ID> the ID type of the aggregate root.
 */
@NotThreadSafe
public abstract class AggregateRoot<ID extends DomainObjectId> extends Entity<ID> {

    @UsedByPersistenceFramework
    private boolean isNew;

    private List<DomainEvent> domainEvents;

    @UsedByPersistenceFramework
    protected AggregateRoot() {
        isNew = false;
    }

    /**
     * Creates a new {@code AggregateRoot} with the given ID.
     *
     * @param id the ID to use.
     */
    protected AggregateRoot(@Nonnull ID id) {
        super(id);
        isNew = true;
    }

    /**
     * Registers the given domain event to be published by the repository when the aggregate root is saved.
     *
     * @param domainEvent the domain event to publish.
     */
    protected void registerDomainEvent(@Nonnull DomainEvent domainEvent) {
        Objects.requireNonNull(domainEvent, "domainEvent must not be null");
        if (domainEvents == null) {
            domainEvents = new ArrayList<>();
        }
        domainEvents.add(domainEvent);
    }

    /**
     * Returns a stream of all registered domain events. This method is intended to be invoked by the repository only.
     */
    @Nonnull
    Stream<DomainEvent> domainEvents() {
        if (domainEvents == null) {
            return Stream.empty();
        } else {
            return domainEvents.stream();
        }
    }

    /**
     * Clears all registered domain events. This method is intended to be invoked by the repository only.
     */
    void clearDomainEvents() {
        if (domainEvents != null) {
            domainEvents.clear();
        }
    }

    /**
     * Returns whether this aggregate root is new, meaning it has not been persisted before.
     */
    public boolean isNew() {
        return isNew;
    }

    /**
     * Sets the value of the {@link #isNew()} flag. This method is intended to be invoked by the repository only.
     */
    void setNew(boolean isNew) {
        this.isNew = isNew;
    }
}
