package net.pkhapps.idispatch.core.domain.incident.model;

import org.jetbrains.annotations.NotNull;

/**
 * Domain event published when an {@link Incident} is opened.
 */
public final class IncidentOpenedEvent extends IncidentEvent {

    IncidentOpenedEvent(@NotNull Incident incident) {
        super(incident);
    }
}
