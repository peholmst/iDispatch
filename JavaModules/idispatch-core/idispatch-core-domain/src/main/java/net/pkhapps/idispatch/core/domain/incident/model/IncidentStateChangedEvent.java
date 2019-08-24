package net.pkhapps.idispatch.core.domain.incident.model;

import org.jetbrains.annotations.NotNull;

/**
 * Domain event published when the state of an already opened {@link Incident} changes.
 */
public final class IncidentStateChangedEvent extends IncidentEvent {

    private final IncidentState state;

    IncidentStateChangedEvent(@NotNull Incident incident) {
        super(incident);
        this.state = incident.state();
    }

    /**
     * The state of the incident.
     */
    public @NotNull IncidentState state() {
        return state;
    }
}
