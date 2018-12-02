package net.pkhapps.idispatch.shared.domain.base;

import org.jetbrains.annotations.NotNull;

import java.time.Instant;

/**
 * Marker interface for domain events.
 */
public interface DomainEvent extends DomainObject {

    /**
     * Returns the date and time on which the domain event occurred.
     */
    @NotNull
    Instant occurredOn();
}
