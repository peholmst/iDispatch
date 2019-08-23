package net.pkhapps.idispatch.core.domain.incident.application;

import net.pkhapps.idispatch.core.domain.common.PhoneNumber;
import net.pkhapps.idispatch.core.domain.geo.Location;
import net.pkhapps.idispatch.core.domain.incident.model.IncidentId;
import net.pkhapps.idispatch.core.domain.incident.model.IncidentPriority;
import net.pkhapps.idispatch.core.domain.incident.model.IncidentState;
import net.pkhapps.idispatch.core.domain.incident.model.IncidentTypeId;
import org.jetbrains.annotations.NotNull;

import java.time.Instant;
import java.util.Optional;

/**
 * Interface defining the details of an incident.
 */
public interface IncidentDetails {

    /**
     * The ID of the incident.
     */
    @NotNull IncidentId id();

    /**
     * The ID of the parent incident, if one exists.
     */
    @NotNull Optional<IncidentId> parent();

    /**
     * The state of the incident.
     */
    @NotNull IncidentState state();

    /**
     * The type of the incident, if categorized.
     */
    @NotNull Optional<IncidentTypeId> type();

    /**
     * The priority of the incident.
     */
    @NotNull IncidentPriority priority();

    /**
     * The location of the incident, if it has been pinpointed.
     */
    @NotNull Optional<Location> location();

    /**
     * Any additional details about the incident.
     */
    @NotNull Optional<String> additionalDetails();

    /**
     * The name of the informer that called in the incident, if available/applicable.
     */
    @NotNull Optional<String> informerName();

    /**
     * The phone number of the informer that called in the incident, if available/applicable.
     */
    @NotNull Optional<PhoneNumber> informerPhoneNumber();

    /**
     * The date and time on which the incident was opened.
     */
    @NotNull Instant opened();

    /**
     * The date and time on which the incident was last modified.
     */
    @NotNull Instant lastModified();

    /**
     * The date and time on which the incident was closed. Only contains a value if {@link #state()} is
     * {@link IncidentState#CLOSED}.
     */
    @NotNull Optional<Instant> closed();

    /**
     * The reason why the incident is on hold. Only contains a value if the {@link #state()} is
     * {@link IncidentState#ON_HOLD}.
     */
    @NotNull Optional<String> holdingReason();
}
