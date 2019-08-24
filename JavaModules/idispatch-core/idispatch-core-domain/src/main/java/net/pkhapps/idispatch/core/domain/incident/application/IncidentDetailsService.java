package net.pkhapps.idispatch.core.domain.incident.application;

import net.pkhapps.idispatch.core.domain.common.ApplicationService;
import net.pkhapps.idispatch.core.domain.common.PhoneNumber;
import net.pkhapps.idispatch.core.domain.geo.Location;
import net.pkhapps.idispatch.core.domain.incident.model.IncidentId;
import net.pkhapps.idispatch.core.domain.incident.model.IncidentNotOpenException;
import net.pkhapps.idispatch.core.domain.incident.model.IncidentPriority;
import net.pkhapps.idispatch.core.domain.incident.model.IncidentTypeId;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;

/**
 * Application service for managing the details of incidents.
 */
public interface IncidentDetailsService extends ApplicationService {

    /**
     * Retrieves the details of all the given incidents. If any of the incidents does not exist, it is quietly
     * left out of the returned stream.
     *
     * @param incidentIds the IDs of the incidents to retrieve.
     * @return a stream of incident detail objects.
     */
    @NotNull Stream<IncidentDetails> retrieveIncidentDetails(@NotNull Iterable<IncidentId> incidentIds);

    /**
     * Retrieves the details of the given incident.
     *
     * @param incidentId the ID of the incident to retrieve.
     * @return the incident details if the incident exists.
     */
    default @NotNull Optional<IncidentDetails> retrieveIncidentDetails(@NotNull IncidentId incidentId) {
        return retrieveIncidentDetails(Set.of(incidentId)).findFirst();
    }

    /**
     * Pinpoints the location of the incident. This method can be called as many times as needed as long as the
     * incident is open.
     *
     * @param incident the incident to pinpoint.
     * @param location the location of the incident.
     * @throws IncidentNotKnownException if the incident does not exist in the system.
     * @throws IncidentNotOpenException  if the incident has already been closed.
     */
    void pinpointIncident(@NotNull IncidentId incident,
                          @NotNull Location location) throws IncidentNotKnownException,
            IncidentNotOpenException;

    /**
     * Categorizes the incident. This method can be called as many times as needed as long as the incident is open.
     *
     * @param type     the type of the incident.
     * @param priority the priority of the incident.
     * @throws IncidentNotKnownException if the incident does not exist in the system.
     * @throws IncidentNotOpenException  if the incident has already been closed.
     */
    void categorizeIncident(@NotNull IncidentTypeId type, @NotNull IncidentPriority priority)
            throws IncidentNotKnownException, IncidentNotOpenException;

    /**
     * Provides any additional information about the incident.  This method can be called as many times as needed as
     * long as the incident is open.
     *
     * @param incident the incident to update.
     * @param details  the additional details of the incident.
     * @throws IncidentNotKnownException if the incident does not exist in the system.
     * @throws IncidentNotOpenException  if the incident has already been closed.
     */
    void provideIncidentDetails(@NotNull IncidentId incident, @Nullable String details) throws IncidentNotKnownException,
            IncidentNotOpenException;

    /**
     * Provides information about the person who called in the incident. This method can be called as many times as
     * needed as long as the incident is open.
     *
     * @param incident    the incident to update.
     * @param name        the name of the informer, if known or applicable.
     * @param phoneNumber the phone number of the informer, if known or applicable.
     * @throws IncidentNotKnownException if the incident does not exist in the system.
     * @throws IncidentNotOpenException  if the incident has already been closed.
     */
    void provideInformerDetails(@NotNull IncidentId incident, @Nullable String name,
                                @Nullable PhoneNumber phoneNumber) throws IncidentNotKnownException,
            IncidentNotOpenException;
}
