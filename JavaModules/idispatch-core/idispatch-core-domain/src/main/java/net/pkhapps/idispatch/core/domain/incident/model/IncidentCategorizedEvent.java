package net.pkhapps.idispatch.core.domain.incident.model;

import org.jetbrains.annotations.NotNull;

/**
 * Domain event published when an {@link Incident} has been categorized.
 */
public final class IncidentCategorizedEvent extends IncidentEvent {

    private final IncidentTypeId type;
    private final IncidentPriority priority;

    IncidentCategorizedEvent(@NotNull Incident incident) {
        super(incident);
        this.type = incident.type().orElseThrow(IllegalStateException::new);
        this.priority = incident.priority();
    }

    /**
     * The type of the incident.
     */
    public @NotNull IncidentTypeId type() {
        return type;
    }

    /**
     * The priority of the incident.
     */
    public @NotNull IncidentPriority priority() {
        return priority;
    }
}
