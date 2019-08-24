package net.pkhapps.idispatch.core.domain.status.application;

import net.pkhapps.idispatch.core.domain.resource.application.ResourceNotKnownException;
import net.pkhapps.idispatch.core.domain.resource.model.ResourceId;
import net.pkhapps.idispatch.core.domain.status.model.StatusId;

import java.util.stream.Stream;

/**
 * Application service for setting and retrieving the statuses of resources.
 */
public interface ResourceStatusService {

    /**
     * Returns the last known statuses of the given {@code resources}. If any resource does not
     * exist in the system or has no known status, it is left out of the result stream.
     */
    Stream<ResourceStatusResponse> retrieveLastKnownStatuses(Iterable<ResourceId> resources);

    /**
     * Sets the current {@code status} of the given {@code resource}.
     *
     * @throws ResourceNotKnownException        if the resource does not exist in the system.
     * @throws IllegalStatusTransitionException if the resource's current status does not allow transitioning to the
     *                                          given {@code status}.
     */
    void setStatus(ResourceId resource, StatusId status) throws ResourceNotKnownException,
            IllegalStatusTransitionException;
}
