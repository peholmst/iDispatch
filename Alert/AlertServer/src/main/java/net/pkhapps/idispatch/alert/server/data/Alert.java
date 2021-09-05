// iDispatch Alert Server
// Copyright (C) 2021 Petter Holmström
//
// This program is free software: you can redistribute it and/or modify
// it under the terms of the GNU Affero General Public License as
// published by the Free Software Foundation, either version 3 of the
// License, or (at your option) any later version.
//
// This program is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
// GNU Affero General Public License for more details.
//
// You should have received a copy of the GNU Affero General Public License
// along with this program.  If not, see <https://www.gnu.org/licenses/>.
package net.pkhapps.idispatch.alert.server.data;

import java.time.Instant;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static java.util.Objects.requireNonNull;

/**
 * An entity representing an alert that will or has been distributed by the system. Once created, alerts are immutable
 * even though they are entities.
 */
public class Alert extends Entity<AlertId> {

    private final Instant alertInstant;

    private final IncidentIdentifier incidentIdentifier;
    private final Instant incidentInstant;
    private final IncidentTypeCode incidentType;
    private final IncidentUrgencyCode incidentUrgency;

    private final Municipality municipality;
    private final GeoPoint coordinates;
    private final Address address;
    private final String details;

    private final Set<ResourceIdentifier> assignedResources;
    private final Set<ResourceIdentifier> resourcesToAlert;

    private Alert(AlertId id, Instant alertInstant, Essence essence) {
        super(id);
        assert essence.isValid();
        this.alertInstant = requireNonNull(alertInstant, "alertInstant must not be null");
        incidentIdentifier = essence.incidentIdentifier;
        incidentInstant = essence.incidentInstant;
        incidentType = essence.incidentType;
        incidentUrgency = essence.incidentUrgency;
        municipality = essence.municipality;
        coordinates = essence.coordinates;
        address = essence.address;
        details = essence.details;
        assignedResources = Set.copyOf(essence.assignedResources);
        resourcesToAlert = Set.copyOf(essence.resourcesToAlert);
    }

    /**
     * An optional identifier that can be used by dispatchers and receivers to identify which incident this alert is
     * concerning.
     *
     * @return an {@code Optional} containing the identifier if there is one.
     */
    public Optional<IncidentIdentifier> incidentIdentifier() {
        return Optional.ofNullable(incidentIdentifier);
    }

    /**
     * An optional timestamp provided by dispatchers that typically is the date and time when an incident was first
     * reported, but could be something else as well. Its purpose is to give receivers some kind of idea of how "fresh"
     * an alert is in relation to the incident it concerns.
     *
     * @return an {@code Optional} containing the instant if there is one.
     */
    public Optional<Instant> incidentInstant() {
        return Optional.ofNullable(incidentInstant);
    }

    /**
     * The timestamp at which the alert was created.
     *
     * @return an {@link Instant}, never {@code null}.
     */
    public Instant alertInstant() {
        return alertInstant;
    }

    /**
     * A code that describes the type of incident that the resources are being alerted to. The code is picked by the
     * dispatcher and receivers should know what this code means.
     *
     * @return an {@link IncidentTypeCode}, never {@code null}.
     */
    public IncidentTypeCode incidentType() {
        return incidentType;
    }

    /**
     * A code that describes the urgency of the incident that the resources are being alerted to. The code is picked by
     * the dispatcher and receivers should know what this code means.
     *
     * @return an {@link IncidentUrgencyCode}, never {@code null}.
     */
    public IncidentUrgencyCode incidentUrgency() {
        return incidentUrgency;
    }

    /**
     * The municipality that the incident occurred in.
     *
     * @return a {@link Municipality}, never {@code null}.
     */
    public Municipality municipality() {
        return municipality;
    }

    /**
     * The coordinates of the incident. These should always be known before any resources are alerted.
     *
     * @return a {@link GeoPoint}, never {@code null}.
     */
    public GeoPoint coordinates() {
        return coordinates;
    }

    /**
     * The human understandable address of the incident if it has one. For example, if the incident is in the middle of
     * the wilderness, it might not have an address at all (however it should always have {@linkplain #coordinates()
     * coordinates}).
     *
     * @return an {@code Optional} containing the {@linkplain Address address} if it has one.
     */
    public Optional<Address> address() {
        return Optional.ofNullable(address);
    }

    /**
     * The details of the alert as provided by the dispatcher (either entered by a human user or generated by the
     * system).
     *
     * @return a possibly empty string, never {@code null}.
     */
    public String details() {
        return details;
    }

    /**
     * All the resources that were assigned to the incident at the time of the alert. This information is included so
     * that newly alerted resources know who else is responding (or who is already on scene).
     *
     * @return a set of {@link ResourceIdentifier}s, always contains at least the {@linkplain #resourcesToAlert()
     * resources to alert}.
     */
    public Set<ResourceIdentifier> assignedResources() {
        return assignedResources;
    }

    /**
     * The resources that will be alerted in response to this particular alert command.
     *
     * @return a set of at least one {@link ResourceIdentifier}.
     */
    public Set<ResourceIdentifier> resourcesToAlert() {
        return resourcesToAlert;
    }

    /**
     * An essence object for creating new {@link Alert}s.
     */
    public static class Essence {

        private final Set<ResourceIdentifier> assignedResources = new HashSet<>();
        private final Set<ResourceIdentifier> resourcesToAlert = new HashSet<>();
        private IncidentIdentifier incidentIdentifier;
        private Instant incidentInstant;
        private IncidentTypeCode incidentType;
        private IncidentUrgencyCode incidentUrgency;
        private Municipality municipality;
        private GeoPoint coordinates;
        private Address address;
        private String details;

        /**
         * Checks if this essence object is valid, meaning it contains all the necessary data to create a valid {@link
         * Alert}.
         */
        public boolean isValid() {
            if (resourcesToAlert.isEmpty()) {
                return false;
            }
            if (!assignedResources.containsAll(resourcesToAlert)) {
                return false;
            }
            if (incidentType == null) {
                return false;
            }
            if (incidentUrgency == null) {
                return false;
            }
            if (municipality == null) {
                return false;
            }
            if (coordinates == null) {
                return false;
            }
            return details != null;
        }

        /**
         * Sets the {@linkplain Alert#incidentType() incident type}.
         *
         * @param incidentType the incident type, must not be {@code null}.
         * @return the essence itself.
         */
        public Essence setIncidentType(IncidentTypeCode incidentType) {
            this.incidentType = requireNonNull(incidentType, "incidentType must not be null");
            return this;
        }

        /**
         * Sets the {@linkplain Alert#incidentUrgency() incident urgency}.
         *
         * @param incidentUrgency the incident urgency, must not be {@code null}.
         * @return the essence itself.
         */
        public Essence setIncidentUrgency(IncidentUrgencyCode incidentUrgency) {
            this.incidentUrgency = requireNonNull(incidentUrgency, "incidentUrgency must not be null");
            return this;
        }

        /**
         * Sets the {@linkplain Alert#municipality() municipality}.
         *
         * @param municipality the municipality, must not be {@code null}.
         * @return the essence itself.
         */
        public Essence setMunicipality(Municipality municipality) {
            this.municipality = requireNonNull(municipality, "municipality must not be null");
            return this;
        }

        /**
         * Sets the {@linkplain Alert#coordinates() coordinates}.
         *
         * @param coordinates the coordinates, must not be {@code null}.
         * @return the essence itself.
         */
        public Essence setCoordinates(GeoPoint coordinates) {
            this.coordinates = requireNonNull(coordinates, "coordinates must not be null");
            return this;
        }

        /**
         * Adds a {@linkplain Alert#resourcesToAlert() resource to alert}. The resource will also be automatically added
         * to the {@linkplain Alert#assignedResources() assigned resources}.
         *
         * @param resourceToAlert the resource that will receive the alert, must not be {@code null}.
         * @return the essence itself.
         */
        public Essence addResourceToAlert(ResourceIdentifier resourceToAlert) {
            requireNonNull(resourceToAlert, "resourceToAlert must not be null");
            this.resourcesToAlert.add(resourceToAlert);
            this.assignedResources.add(resourceToAlert);
            return this;
        }

        /**
         * Adds an {@linkplain Alert#assignedResources() assigned resource} that is assigned to the incident but will
         * not receive an alert at this time (for example, if it has already been alerted earlier).
         *
         * @param assignedResource the assigned resource, must not be {@code null}.
         * @return the essence itself.
         */
        public Essence addAssignedResource(ResourceIdentifier assignedResource) {
            requireNonNull(assignedResource, "assignedResource must not be null");
            this.assignedResources.add(assignedResource);
            return this;
        }

        /**
         * Sets the {@linkplain Alert#address() address} of the incident that the resources will be alerted to.
         *
         * @param address the address, may be {@code null}.
         * @return the essence itself.
         */
        public Essence setAddress(Address address) {
            this.address = address;
            return this;
        }

        /**
         * Sets the {@linkplain Alert#details() details} of the alert.
         *
         * @param details the details, {@code null} will be converted to an empty string.
         * @return the essence itself.
         */
        public Essence setDetails(String details) {
            this.details = details == null ? "" : details;
            return this;
        }

        /**
         * Sets the {@linkplain Alert#incidentIdentifier() incident identifier}.
         *
         * @param incidentIdentifier the incident identifier, may be {@code null}.
         * @return the essence itself.
         */
        public Essence setIncidentIdentifier(IncidentIdentifier incidentIdentifier) {
            this.incidentIdentifier = incidentIdentifier;
            return this;
        }

        /**
         * Sets the {@linkplain Alert#incidentInstant() incident instant}.
         *
         * @param incidentInstant the incident instant, may be {@code null}.
         * @return the essence itself.
         */
        public Essence setIncidentInstant(Instant incidentInstant) {
            this.incidentInstant = incidentInstant;
            return this;
        }

        /**
         * Creates a new {@code Alert} entity from this essence. You can change the essence afterwards without affecting
         * already created entities.
         *
         * @param alertId      the ID that the new entity will receive, must not be {@code null}.
         * @param alertInstant the instant at which the alert was created, must not be {@code null}.
         * @return a new {@code Alert}.
         */
        public Alert createAlert(AlertId alertId, Instant alertInstant) {
            if (!isValid()) {
                throw new IllegalStateException("The essence object is not valid and cannot be used to create an Alert object");
            }
            return new Alert(alertId, alertInstant, this);
        }
    }
}
