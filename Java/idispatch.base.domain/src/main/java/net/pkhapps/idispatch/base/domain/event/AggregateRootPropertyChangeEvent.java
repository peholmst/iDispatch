package net.pkhapps.idispatch.base.domain.event;

import net.pkhapps.idispatch.base.domain.AggregateRoot;
import org.springframework.lang.NonNull;

import java.time.Clock;
import java.time.Instant;

/**
 * Base class for domain events that are fired when a single property of an aggregate root is changed.
 *
 * @param <P> the type of the property.
 * @param <T> the type of the aggregate root.
 */
public abstract class AggregateRootPropertyChangeEvent<P, T extends AggregateRoot<?>> extends AggregateRootDomainEvent<T> {

    private final P oldValue;
    private final P newValue;

    protected AggregateRootPropertyChangeEvent(@NonNull T sender, @NonNull Instant occurredOn, P oldValue, P newValue) {
        super(sender, occurredOn);
        this.oldValue = oldValue;
        this.newValue = newValue;
    }

    protected AggregateRootPropertyChangeEvent(@NonNull T sender, @NonNull Clock clock, P oldValue, P newValue) {
        this(sender, clock.instant(), oldValue, newValue);
    }

    public P getOldValue() {
        return oldValue;
    }

    public P getNewValue() {
        return newValue;
    }
}
