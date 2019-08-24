package net.pkhapps.idispatch.core.domain.status.application;

import net.pkhapps.idispatch.core.domain.resource.application.ResourceNotKnownException;
import net.pkhapps.idispatch.core.domain.resource.model.ResourceId;
import org.opengis.geometry.DirectPosition;

import java.util.stream.Stream;

/**
 * Application service for setting and retrieving the geographical locations of resources.
 */
public interface ResourceLocationService {

    /**
     * Returns the last known locations of the given {@code resources}. If any resource does not
     * exist in the system or has no known location, it is left out of the result stream.
     */
    Stream<ResourceLocationResponse> retrieveLastKnownLocations(Iterable<ResourceId> resources);

    /**
     * Sets the current {@code location} of the given {@code resource}.
     *
     * @throws ResourceNotKnownException if the resource does not exist in the system.
     */
    void setLocation(ResourceId resource, DirectPosition location) throws ResourceNotKnownException;
}
