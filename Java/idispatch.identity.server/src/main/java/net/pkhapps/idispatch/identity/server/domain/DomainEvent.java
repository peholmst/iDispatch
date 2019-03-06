package net.pkhapps.idispatch.identity.server.domain;

import lombok.Getter;

import java.io.Serializable;
import java.time.Instant;

/**
 * Base class for domain events (typically published by {@link AggregateRoot}s).
 */
@Getter
public abstract class DomainEvent implements Serializable {

    private final Instant occurredOn;

    public DomainEvent() {
        this.occurredOn = DomainServices.getInstance().clock().instant();
    }
}
