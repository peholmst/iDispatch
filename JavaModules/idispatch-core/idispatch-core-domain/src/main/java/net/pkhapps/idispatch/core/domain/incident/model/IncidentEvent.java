package net.pkhapps.idispatch.core.domain.incident.model;

import net.pkhapps.idispatch.core.domain.common.DomainContext;
import net.pkhapps.idispatch.core.domain.common.DomainEvent;
import org.jetbrains.annotations.NotNull;

import java.time.Instant;

import static java.util.Objects.requireNonNull;

/**
 * Base class for domain events published by an {@link Incident}.
 */
public abstract class IncidentEvent implements DomainEvent {

    private final IncidentId incident;
    private final Instant occurredOn;

    IncidentEvent(@NotNull Incident incident) {
        this(requireNonNull(incident).id(), DomainContext.instance().clock().instant());
    }

    IncidentEvent(@NotNull IncidentId incident, @NotNull Instant occurredOn) {
        this.incident = requireNonNull(incident);
        this.occurredOn = requireNonNull(occurredOn);
    }

    /**
     * The ID of the incident that published this domain event.
     */
    public @NotNull IncidentId incident() {
        return incident;
    }

    @Override
    public @NotNull Instant occurredOn() {
        return occurredOn;
    }
}
