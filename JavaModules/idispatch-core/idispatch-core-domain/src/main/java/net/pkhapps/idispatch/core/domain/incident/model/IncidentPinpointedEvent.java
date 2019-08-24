package net.pkhapps.idispatch.core.domain.incident.model;

import net.pkhapps.idispatch.core.domain.geo.Location;
import org.jetbrains.annotations.NotNull;

/**
 * Domain event published when an {@link Incident} has been pinpointed.
 */
public final class IncidentPinpointedEvent extends IncidentEvent {

    private final Location location;

    IncidentPinpointedEvent(@NotNull Incident incident) {
        super(incident);
        this.location = incident.location().orElseThrow(IllegalStateException::new);
    }

    /**
     * The location of the incident.
     */
    public @NotNull Location location() {
        return location;
    }
}
