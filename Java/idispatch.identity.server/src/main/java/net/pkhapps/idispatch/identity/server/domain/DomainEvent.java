package net.pkhapps.idispatch.identity.server.domain;

import org.springframework.lang.NonNull;

import java.io.Serializable;
import java.time.Instant;

/**
 * Base class for domain events (typically published by {@link AggregateRoot}s).
 */
public abstract class DomainEvent implements Serializable {

    private final Instant occurredOn;

    public DomainEvent() {
        this.occurredOn = DomainServices.getInstance().clock().instant();
    }

    @NonNull
    public Instant getOccurredOn() {
        return occurredOn;
    }
}
