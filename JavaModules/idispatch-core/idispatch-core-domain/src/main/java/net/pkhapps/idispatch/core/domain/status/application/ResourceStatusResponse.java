package net.pkhapps.idispatch.core.domain.status.application;

import net.pkhapps.idispatch.core.domain.resource.model.ResourceId;
import net.pkhapps.idispatch.core.domain.status.model.StatusId;

import java.time.Instant;

/**
 * Interface defining a response DTO for the status of a resource.
 */
public interface ResourceStatusResponse {
    /**
     * Returns the resource.
     */
    ResourceId resource();

    /**
     * Returns the status of the resource.
     */
    StatusId status();

    /**
     * Returns the date and time at which the {@link #resource()} had the given {@link #status()}.
     */
    Instant timestamp();
}
