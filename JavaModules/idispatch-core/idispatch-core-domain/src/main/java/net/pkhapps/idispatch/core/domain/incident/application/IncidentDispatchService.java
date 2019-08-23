package net.pkhapps.idispatch.core.domain.incident.application;

import net.pkhapps.idispatch.core.domain.common.ApplicationService;
import net.pkhapps.idispatch.core.domain.incident.model.IncidentId;
import net.pkhapps.idispatch.core.domain.resource.model.ResourceId;
import org.jetbrains.annotations.NotNull;

/**
 * Application service for dispatching resources to incidents.
 */
public interface IncidentDispatchService extends ApplicationService {

    /**
     * Dispatches all resources that are currently attached to the given incident, regardless of whether they have
     * been dispatched before.
     *
     * @param incident the incident to dispatch the resources to.
     * @throws IncidentNotKnownException                if the incident does not exist in the system.
     * @throws InsufficientIncidentInformationException if the incident does not yet contain enough information to allow dispatching.
     * @throws IncidentNotOpenException                 if the incident has been closed.
     */
    void dispatchAllResources(@NotNull IncidentId incident) throws IncidentNotKnownException,
            InsufficientIncidentInformationException, IncidentNotOpenException;

    /**
     * Dispatches all resources that are currently attached to the given incident and that have not yet been
     * dispatched.
     *
     * @param incident the incident to dispatch the resources to.
     * @throws IncidentNotKnownException                if the incident does not exist in the system.
     * @throws InsufficientIncidentInformationException if the incident does not yet contain enough information to allow dispatching.
     * @throws IncidentNotOpenException                 if the incident has been closed.
     */
    void dispatchNewResources(@NotNull IncidentId incident) throws IncidentNotKnownException,
            InsufficientIncidentInformationException, IncidentNotOpenException;

    /**
     * Dispatches the given resources to the given incident, regardless of whether they have been dispatched before. If
     * any of the resources are not attached to the incident in question or do not exist, they will be silently
     * ignored.
     *
     * @param incident  the incident to dispatch the resources to.
     * @param resources the IDs of the resources to dispatch.
     * @throws IncidentNotKnownException                if the incident does not exist in the system.
     * @throws InsufficientIncidentInformationException if the incident does not yet contain enough information to allow dispatching.
     * @throws IncidentNotOpenException                 if the incident has been closed.
     */
    void dispatchSelectedResources(@NotNull IncidentId incident, @NotNull Iterable<ResourceId> resources)
            throws IncidentNotKnownException, InsufficientIncidentInformationException, IncidentNotOpenException;
}
