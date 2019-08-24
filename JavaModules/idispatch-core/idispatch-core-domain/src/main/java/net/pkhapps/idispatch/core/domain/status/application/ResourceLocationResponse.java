package net.pkhapps.idispatch.core.domain.status.application;

import net.pkhapps.idispatch.core.domain.resource.model.ResourceId;
import org.opengis.geometry.DirectPosition;

import java.time.Instant;

/**
 * Interface defining a response DTO for the geographical location of a resource.
 */
public interface ResourceLocationResponse {
    /**
     * Returns the resource.
     */
    ResourceId resource();

    /**
     * Returns the geographical location of the resource.
     */
    DirectPosition location();

    /**
     * Returns the date and time at which the {@link #resource()} was at the {@link #location()}.
     */
    Instant timestamp();
}
