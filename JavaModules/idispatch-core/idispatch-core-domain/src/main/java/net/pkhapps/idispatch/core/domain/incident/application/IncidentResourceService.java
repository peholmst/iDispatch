package net.pkhapps.idispatch.core.domain.incident.application;

import net.pkhapps.idispatch.core.domain.common.ApplicationService;
import net.pkhapps.idispatch.core.domain.incident.model.IncidentId;
import net.pkhapps.idispatch.core.domain.resource.application.ResourceNotKnownException;
import net.pkhapps.idispatch.core.domain.resource.model.ResourceId;
import org.jetbrains.annotations.NotNull;

import java.util.stream.Stream;

/**
 * Application service for managing the resources that are attached to incidents.
 */
public interface IncidentResourceService extends ApplicationService {

    /**
     * Attaches the given resource to the given incident. If the resource is already attached to the incident, nothing
     * happens. If the resource is attached to another incident, it will be automatically detached from it.
     *
     * @param incident the incident to attach the resource to.
     * @param resource the resource to attach.
     * @throws ResourceNotKnownException if the resource does not exist in the system.
     * @throws IncidentNotKnownException if the incident does not exist in the system.
     * @throws IncidentNotOpenException  if the incident is closed.
     */
    void attachToIncident(@NotNull IncidentId incident, @NotNull ResourceId resource) throws ResourceNotKnownException,
            IncidentNotKnownException, IncidentNotOpenException;

    /**
     * Detaches the given resource from the given incident. If the resource is not attached to the incident or
     * does not exist, nothing happens.
     *
     * @param incident the incident to detach the resource from.
     * @param resource the resource to detach.
     * @throws IncidentNotKnownException if the incident does not exist in the system.
     * @throws IncidentNotOpenException  if the incident is closed.
     */
    void detachFromIncident(@NotNull IncidentId incident, @NotNull ResourceId resource)
            throws IncidentNotKnownException, IncidentNotOpenException;

    /**
     * Retrieves all resources currently attached to the given incident.
     *
     * @param incident the incident whose resources should be retrieved.
     * @return a stream of attached resources.
     * @throws IncidentNotKnownException if the incident does not exist in the system.
     * @throws IncidentNotOpenException  if the incident is closed.
     */
    @NotNull Stream<AttachedResource> retrieveAttachedResources(@NotNull IncidentId incident)
            throws IncidentNotKnownException, IncidentNotOpenException;
}
