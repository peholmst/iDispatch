package net.pkhapps.idispatch.core.domain.incident.model;

import org.jetbrains.annotations.NotNull;

/**
 * Domain event published when the details of an {@link Incident} have been updated.
 */
public final class IncidentDetailsUpdatedEvent extends IncidentEvent {

    private final String details;

    IncidentDetailsUpdatedEvent(@NotNull Incident incident) {
        super(incident);
        this.details = incident.details().orElseThrow(IllegalStateException::new);
    }

    /**
     * The details of the incident.
     */
    public @NotNull String details() {
        return details;
    }
}
