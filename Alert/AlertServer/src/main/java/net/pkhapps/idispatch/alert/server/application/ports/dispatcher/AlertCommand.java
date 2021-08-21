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
package net.pkhapps.idispatch.alert.server.application.ports.dispatcher;

import static java.util.Objects.requireNonNull;

import java.time.Instant;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import net.pkhapps.idispatch.alert.server.domain.model.Address;
import net.pkhapps.idispatch.alert.server.domain.model.GeoPoint;
import net.pkhapps.idispatch.alert.server.domain.model.IncidentIdentifier;
import net.pkhapps.idispatch.alert.server.domain.model.IncidentTypeCode;
import net.pkhapps.idispatch.alert.server.domain.model.IncidentUrgencyCode;
import net.pkhapps.idispatch.alert.server.domain.model.Municipality;
import net.pkhapps.idispatch.alert.server.domain.model.ResourceIdentifier;

/**
 * A data object describing an alert command.
 */
public class AlertCommand {

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

    private AlertCommand(Builder builder) {
        if (builder.resourcesToAlert.isEmpty()) {
            throw new IllegalArgumentException("resourceToAlert should contain at least one resource");
        }
        if (!builder.assignedResources.containsAll(builder.resourcesToAlert)) {
            throw new IllegalArgumentException("All resourcesToAlert should also be in the assignedResources set");
        }
        this.incidentIdentifier = builder.incidentIdentifier;
        this.incidentInstant = builder.incidentInstant;
        this.incidentType = requireNonNull(builder.incidentType, "incidentType cannot be null");
        this.incidentUrgency = requireNonNull(builder.incidentUrgency, "incidentUrgency cannot be null");
        this.municipality = requireNonNull(builder.municipality, "municipality cannot be null");
        this.coordinates = requireNonNull(builder.coordinates, "coordinates cannot be null");
        this.address = builder.address;
        this.details = requireNonNull(builder.details, "details cannot be null");
        this.assignedResources = Set.copyOf(builder.assignedResources);
        this.resourcesToAlert = Set.copyOf(builder.resourcesToAlert);
    }

    /**
     * An optional identifier that can be used by dispatchers and receivers to
     * identify which indicent this alert is concerning.
     * 
     * @return an {@code Optional} containing the identifier if there is one.
     */
    public Optional<IncidentIdentifier> getIncidentIdentifier() {
        return Optional.ofNullable(incidentIdentifier);
    }

    /**
     * An optional timestamp provided by dispatchers that typically is the date and
     * time when an incident was first reported, but could be something else as
     * well. Its purpose is to give receivers some kind of an idea of how "fresh" an
     * alert is in relation to the incident it concerns.
     * 
     * @return an {@code Optional} containing the instant if there is one.
     */
    public Optional<Instant> getIncidentInstant() {
        return Optional.ofNullable(incidentInstant);
    }

    /**
     * A code that describes the type of incident that the resources are being
     * alerted to. The code is picked by the dispatcher and receivers should know
     * what this code means.
     * 
     * @return an {@link IncidentTypeCode}, never {@code null}.
     */
    public IncidentTypeCode getIncidentType() {
        return incidentType;
    }

    /**
     * A code that describes the urgency of the incident that the resources are
     * being alerted to. The code is picked by the dispatcher and receievers should
     * know what this code means.
     * 
     * @return an {@link IncidentUrgencyCode}, never {@code null}.
     */
    public IncidentUrgencyCode getIncidentUrgency() {
        return incidentUrgency;
    }

    /**
     * The municipality that the incident occurred in.
     * 
     * @return a {@link Municipality}, never {@code null}.
     */
    public Municipality getMunicipality() {
        return municipality;
    }

    /**
     * The coordinates of the incident. These should always be known before any
     * resources are alerted.
     * 
     * @return a {@link GeoPoint}, never {@code null}.
     */
    public GeoPoint getCoordinates() {
        return coordinates;
    }

    /**
     * The human understandable address of the incident if it has one. For example,
     * if the incident is in the middle of the wilderness, it might not have an
     * address at all (however it should always have {@linkplain #getCoordinates()
     * coordinates}).
     * 
     * @return an {@code Optional} containing the {@linkplain Address address} if it
     *         has one.
     */
    public Optional<Address> getAddress() {
        return Optional.ofNullable(address);
    }

    /**
     * The details of the alert as provided by the dispatcher (either entered by a
     * human user or generated by the system).
     * 
     * @return a possibly empty string, never {@code null}.
     */
    public String getDetails() {
        return details;
    }

    /**
     * All the resources that were assigned to the incident at the time of the
     * alert. This information is included so that newly alerted resources know who
     * else is responding (or who is alredy on scene).
     * 
     * @return a set of {@link ResourceIdentifier}s, always contains at least the
     *         {@linkplain #getResourcesToAlert() resources to alert}.
     */
    public Set<ResourceIdentifier> getAssignedResources() {
        return assignedResources;
    }

    /**
     * The resources that will be alerted in response to this particular alert
     * command.
     * 
     * @return a set of at least one {@link ResourceIdentifier}.
     */
    public Set<ResourceIdentifier> getResourcesToAlert() {
        return resourcesToAlert;
    }

    /**
     * Creates a builder for building a new {@link AlertCommand} object.
     * 
     * @return a new {@link Builder} instance.
     */
    public static Builder builder() {
        return new Builder();
    }

    /**
     * Builder class for building new {@link AlertCommand} objects.
     * 
     * @see AlertCommand#builder()
     */
    public static class Builder {

        private IncidentIdentifier incidentIdentifier;
        private Instant incidentInstant;
        private IncidentTypeCode incidentType;
        private IncidentUrgencyCode incidentUrgency;
        private Municipality municipality;
        private GeoPoint coordinates;
        private Address address;
        private String details = "";
        private final Set<ResourceIdentifier> assignedResources = new HashSet<>();
        private final Set<ResourceIdentifier> resourcesToAlert = new HashSet<>();

        private Builder() {
        }

        /**
         * Sets the {@linkplain AlertCommand#getIncidentType() incident type} of the
         * command object.
         * 
         * @param incidentType the incident type, must not be {@code null}.
         * @return the builder itself.
         */
        public Builder withIncidentType(IncidentTypeCode incidentType) {
            this.incidentType = requireNonNull(incidentType, "incidentType cannot be null");
            return this;
        }

        /**
         * Sets the {@linkplain AlertCommand#getIncidentUrgency() incident urgency} of
         * the command object.
         * 
         * @param incidentUrgency the incident urgency, must not be {@code null}.
         * @return the builder itself.
         */
        public Builder withIncidentUrgency(IncidentUrgencyCode incidentUrgency) {
            this.incidentUrgency = requireNonNull(incidentUrgency, "incidentUrgency cannot be null");
            return this;
        }

        /**
         * Sets the {@linkplain AlertCommand#getMunicipality() municipality} of the
         * command object.
         * 
         * @param municipality the municipality, must not be {@code null}.
         * @return the builder itself.
         */
        public Builder withMunicipality(Municipality municipality) {
            this.municipality = requireNonNull(municipality, "municipality cannot be null");
            return this;
        }

        /**
         * Sets the {@linkplain AlertCommand#getCoordinates() coordinates} of the
         * command object.
         * 
         * @param coordinates the coordinates, must not be {@code null}.
         * @return the builder itself.
         */
        public Builder withCoordinates(GeoPoint coordinates) {
            this.coordinates = requireNonNull(coordinates, "coordinates cannot be null");
            return this;
        }

        /**
         * Adds a {@linkplain AlertCommand#getResourcesToAlert() resource to alert} to
         * the command object. The resource will also be automatically added to the
         * {@linkplain AlertCommand#getAssignedResources() assigned resources}.
         * 
         * @param resourceToAlert the resource that will receive the alert, must not be
         *                        {@code null}.
         * @return the builder itself.
         */
        public Builder withResourceToAlert(ResourceIdentifier resourceToAlert) {
            requireNonNull(resourceToAlert, "resourceToAlert cannot be null");
            this.resourcesToAlert.add(resourceToAlert);
            this.assignedResources.add(resourceToAlert);
            return this;
        }

        /**
         * Adds an {@linkplain AlertCommand#getAssignedResources() assigned resource}
         * that is assigned to the incident but will not receive an alert at this time
         * (for example, if it has already been alerted earlier).
         * 
         * @param assignedResource the assigned resource, must not be {@code null}.
         * @return the builder itself.
         */
        public Builder withAssignedResource(ResourceIdentifier assignedResource) {
            requireNonNull(assignedResource, "assignedResource cannot be null");
            this.assignedResources.add(assignedResource);
            return this;
        }

        /**
         * Sets the {@linkplain AlertCommand#getAddress() address} of the incident that
         * the resources will be alerted to.
         * 
         * @param address the address, may be {@code null}.
         * @return the builder itself.
         */
        public Builder withAddress(Address address) {
            this.address = address;
            return this;
        }

        /**
         * Sets the {@linkplain AlertCommand#getDetails() details} of the alert.
         * 
         * @param details the details, {@code null} will be converted to an empty
         *                string.
         * @return the builder itself.
         */
        public Builder withDetails(String details) {
            this.details = details == null ? "" : details;
            return this;
        }

        /**
         * Sets the {@linkplain AlertCommand#getIncidentIdentifier() incident identifier
         * of the alert.
         * 
         * @param incidentIdentifier the incident identifier, may be {@code null}.
         * @return the builder itself.
         */
        public Builder withIncidentIdentifier(IncidentIdentifier incidentIdentifier) {
            this.incidentIdentifier = incidentIdentifier;
            return this;
        }

        /**
         * Sets the {@linkplain AlertCommand#getIncidentInstant() incident instant} of
         * the alert.
         * 
         * @param incidentInstant the incident instant, may be {@code null}.
         * @return the builder itself.
         */
        public Builder withIncidentInstant(Instant incidentInstant) {
            this.incidentInstant = incidentInstant;
            return this;
        }

        /**
         * Builds a new {@link AlertCommand} object. This method can be invoked multiple
         * times and will yield a new instance every time.
         * 
         * @return a new {@link AlertCommand} object.
         */
        public AlertCommand build() {
            return new AlertCommand(this);
        }
    }
}
