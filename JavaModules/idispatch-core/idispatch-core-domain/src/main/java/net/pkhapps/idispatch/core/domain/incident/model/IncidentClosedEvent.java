package net.pkhapps.idispatch.core.domain.incident.model;

import org.jetbrains.annotations.NotNull;

/**
 * Domain event published when an {@link Incident} is closed.
 */
public final class IncidentClosedEvent extends IncidentEvent {

    IncidentClosedEvent(@NotNull Incident incident) {
        super(incident);
    }
}
