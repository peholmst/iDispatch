package net.pkhapps.idispatch.core.domain.incident.application;

import net.pkhapps.idispatch.core.domain.common.ApplicationService;
import net.pkhapps.idispatch.core.domain.incident.model.IncidentId;
import org.jetbrains.annotations.NotNull;

/**
 * Application service for managing the lifecycle of incidents.
 */
public interface IncidentLifecycleService extends ApplicationService {

    /**
     * Opens a new incident.
     *
     * @return the ID of the newly opened incident.
     */
    @NotNull IncidentId openIncident();

    /**
     * Opens a sub-incident to the given parent incident. The sub-incident can have its own lifecycle with the
     * exception that it has to be closed before the parent incident can be closed.
     *
     * @param parentIncident the parent incident.
     * @return the ID of the newly opened sub-incident.
     * @throws IncidentNotKnownException if the parent incident does not exist in the system.
     * @throws IncidentNotOpenException  if the parent incident has already been closed.
     */
    @NotNull IncidentId openSubIncident(@NotNull IncidentId parentIncident) throws IncidentNotKnownException,
            IncidentNotOpenException;

    /**
     * Puts the given incident on hold.
     *
     * @param incident the incident to put on hold.
     * @param reason   the reason why the incident was put on hold.
     * @throws IncidentNotKnownException                if the incident does not exist in the system.
     * @throws IncidentNotOpenException                 if the incident has already been closed.
     * @throws InsufficientIncidentInformationException if the incident does not yet contain enough information to be
     *                                                  put on hold.
     */
    void putIncidentOnHold(@NotNull IncidentId incident, @NotNull String reason) throws IncidentNotKnownException,
            IncidentNotOpenException, InsufficientIncidentInformationException;

    /**
     * Closes the given incident. After an incident has been closed, it can no longer be changed or interacted with
     * in any way.
     *
     * @param incident the ID of the incident.
     * @throws IncidentNotKnownException   if the incident does not exist in the system.
     * @throws IncidentNotClearedException if the incident has not been cleared yet
     *                                     (there are still attached resources or open sub-incidents).
     * @throws IncidentNotOpenException    if the incident has already been closed.
     */
    void closeIncident(@NotNull IncidentId incident) throws IncidentNotKnownException, IncidentNotClearedException,
            IncidentNotOpenException;
}
