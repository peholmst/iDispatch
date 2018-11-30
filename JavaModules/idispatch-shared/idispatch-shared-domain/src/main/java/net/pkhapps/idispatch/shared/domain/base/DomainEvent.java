package net.pkhapps.idispatch.shared.domain.base;

import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.time.Instant;

/**
 * Marker interface for domain events.
 */
public interface DomainEvent extends Serializable {

    /**
     * Returns the date and time on which the domain event occurred.
     */
    @NotNull
    Instant getOccurredOn();
}
