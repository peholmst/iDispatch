package net.pkhapps.idispatch.core.domain.incident.application;

import net.pkhapps.idispatch.core.domain.incident.model.IncidentId;
import net.pkhapps.idispatch.core.domain.resource.model.ResourceId;
import org.jetbrains.annotations.NotNull;

import java.time.Instant;

/**
 * Interface defining a resource that is attached to an incident.
 */
public interface AttachedResource {

    /**
     * The ID of the incident that the resource is attached to.
     */
    @NotNull IncidentId incident();

    /**
     * The ID of the resource itself.
     */
    @NotNull ResourceId resource();

    /**
     * The date and time at which the resource was attached to the incident.
     */
    @NotNull Instant attachedSince();
}
