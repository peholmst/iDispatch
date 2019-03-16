package net.pkhapps.idispatch.base.domain;

import org.springframework.lang.NonNull;

import java.time.Instant;

/**
 * Interface for domain events.
 */
public interface DomainEvent extends DomainObject {

    /**
     * Returns the date and time on which the domain event occurred.
     */
    @NonNull
    Instant getOccurredOn();
}
