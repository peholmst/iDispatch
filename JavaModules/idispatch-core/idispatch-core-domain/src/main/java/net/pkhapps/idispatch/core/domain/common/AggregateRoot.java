package net.pkhapps.idispatch.core.domain.common;

import org.jetbrains.annotations.NotNull;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * Base class for aggregate roots.
 */
public abstract class AggregateRoot<ID> extends Entity<ID> {

    private long optLockVersion = 0;
    private Instant createdOn;
    private Instant lastModifiedOn;

    private List<DomainEvent> domainEvents;

    protected AggregateRoot(@NotNull ID id) {
        super(id);
    }

    protected AggregateRoot(@NotNull Essence<ID> essence) {
        super(essence);
        optLockVersion = essence.optLockVersion;
        createdOn = essence.createdOn;
        lastModifiedOn = essence.lastModifiedOn;
    }

    /**
     * The optimistic locking version of this aggregate. This number will be increased every time the aggregate is
     * persisted. If the aggregate has not been persisted yet, this property is 0.
     */
    public long optLockVersion() {
        return optLockVersion;
    }

    /**
     * The date and time on which the aggregate was created. If the aggregate has not been persisted yet, this property
     * is empty.
     */
    public @NotNull Optional<Instant> createdOn() {
        return Optional.ofNullable(createdOn);
    }

    /**
     * The date and time on which the aggregate was last modified. If the aggregate has never been modified, this
     * is the same as {@link #createdOn()}.
     */
    public @NotNull Optional<Instant> lastModifiedOn() {
        return Optional.ofNullable(lastModifiedOn);
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

    /**
     * Base class designed to be used by repositories to re-create entities using the essence pattern, without needing
     * to go through cumbersome constructors or invoking business methods.
     */
    public static abstract class Essence<ID> extends Entity.Essence<ID> {

        private long optLockVersion;
        private Instant createdOn;
        private Instant lastModifiedOn;

        public Essence() {
        }

        public Essence(@NotNull AggregateRoot<ID> source) {
            super(source);
            optLockVersion = source.optLockVersion;
            createdOn = source.createdOn;
            lastModifiedOn = source.lastModifiedOn;
        }

        public long getOptLockVersion() {
            return optLockVersion;
        }

        public void setOptLockVersion(long optLockVersion) {
            this.optLockVersion = optLockVersion;
        }

        public void incrementOptLockVersion() {
            optLockVersion++;
        }

        public Instant getCreatedOn() {
            return createdOn;
        }

        public void setCreatedOn(Instant createdOn) {
            this.createdOn = createdOn;
        }

        public Instant getLastModifiedOn() {
            return lastModifiedOn;
        }

        public void setLastModifiedOn(Instant lastModifiedOn) {
            this.lastModifiedOn = lastModifiedOn;
        }
    }
}
