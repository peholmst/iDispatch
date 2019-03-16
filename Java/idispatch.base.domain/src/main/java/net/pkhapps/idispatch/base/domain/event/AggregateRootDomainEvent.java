package net.pkhapps.idispatch.base.domain.event;

import net.pkhapps.idispatch.base.domain.AggregateRoot;
import net.pkhapps.idispatch.base.domain.DomainEvent;
import org.springframework.lang.NonNull;

import java.time.Clock;
import java.time.Instant;

import static java.util.Objects.requireNonNull;

/**
 * Base class for domain events that originate from an aggregate root.
 */
public abstract class AggregateRootDomainEvent<T extends AggregateRoot<?>> implements DomainEvent {

    private final T sender;

    private final Instant occurredOn;

    protected AggregateRootDomainEvent(@NonNull T sender, @NonNull Instant occurredOn) {
        this.sender = requireNonNull(sender);
        this.occurredOn = requireNonNull(occurredOn);
    }

    protected AggregateRootDomainEvent(@NonNull T sender, @NonNull Clock clock) {
        this(sender, clock.instant());
    }

    /**
     * Returns the aggregate root that published the domain event.
     */
    @NonNull
    public T getSender() {
        return sender;
    }

    @Override
    @NonNull
    public Instant getOccurredOn() {
        return occurredOn;
    }
}
