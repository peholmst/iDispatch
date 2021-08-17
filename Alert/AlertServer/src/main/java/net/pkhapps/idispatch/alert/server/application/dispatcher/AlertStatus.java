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
package net.pkhapps.idispatch.alert.server.application.dispatcher;

import static java.util.Objects.requireNonNull;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import net.pkhapps.idispatch.alert.server.domain.model.AlertId;
import net.pkhapps.idispatch.alert.server.domain.model.IncidentIdentifier;
import net.pkhapps.idispatch.alert.server.domain.model.ResourceIdentifier;

/**
 * A data object describing the status of a specific alert.
 */
public class AlertStatus {

    private final AlertId alertId;
    private final IncidentIdentifier incidentIdentifier;
    private final Map<ResourceIdentifier, AlertedResourceStatus> resources;

    private AlertStatus(Builder builder) {
        if (builder.resources.isEmpty()) {
            throw new IllegalArgumentException("resources should contain at least one entry");
        }
        this.alertId = requireNonNull(builder.alertId, "alertId cannot be null");
        this.incidentIdentifier = builder.incidentIdentifier;
        this.resources = Map.copyOf(builder.resources);
    }

    /**
     * The ID of the alert.
     * 
     * @return an {@link AlertId}, never {@code null}.
     */
    public AlertId getAlertId() {
        return alertId;
    }

    /**
     * An optional identifier that can be used by dispatchers and receivers to
     * identify which incident this alert is concerning.
     * 
     * @return an {@code Optional} containing the identifier if there is one.
     */
    public Optional<IncidentIdentifier> getIncidentIdentifier() {
        return Optional.ofNullable(incidentIdentifier);
    }

    /**
     * The status of all the alerted resources.
     * 
     * @return an unmodifiable map of each alerted resource and its corresponding
     *         status object.
     */
    public Map<ResourceIdentifier, AlertedResourceStatus> getResources() {
        return resources;
    }

    /**
     * Creates a builder for building a new {@link AlertStatus} object.
     * 
     * @return a new {@link Builder} instance.
     */
    public static Builder builder() {
        return new Builder();
    }

    /**
     * Builder class for building new {@link AlertStatus} objects.
     * 
     * @see AlertStatus#builder()
     */
    public static class Builder {

        private AlertId alertId;
        private IncidentIdentifier incidentIdentifier;
        private final Map<ResourceIdentifier, AlertedResourceStatus> resources = new HashMap<>();

        private Builder() {
        }

        /**
         * Sets the {@linkplain AlertStatus#getAlertId() alert ID} of the status object.
         * 
         * @param alertId the alert ID, must not be {@code null}.
         * @return the builder itself.
         */
        public Builder withAlertId(AlertId alertId) {
            this.alertId = requireNonNull(alertId, "alertId cannot be null");
            return this;
        }

        /**
         * Sets the {@linkplain AlertStatus#getIncidentIdentifier() incident identifier}
         * of the status object.
         * 
         * @param incidentIdentifier the incident identifier, may be {@code null}.
         * @return the builder itself.
         */
        public Builder withIncidentIdentifier(IncidentIdentifier incidentIdentifier) {
            this.incidentIdentifier = incidentIdentifier;
            return this;
        }

        /**
         * Adds an alerted resource, that has not yet received the alert, to the status
         * object.
         * 
         * @param resourceIdentifier the identifier of the alerted resource, must not be
         *                           {@code null}.
         * @param alertSent          the instant at which the alert was sent to the
         *                           resource, must not be {@code null}.
         * @return the builder itself.
         */
        public Builder withAlertedResource(ResourceIdentifier resourceIdentifier, Instant alertSent) {
            requireNonNull(resourceIdentifier, "resourceIdentifier cannot be null");
            requireNonNull(alertSent, "alertSent cannot be null");
            resources.put(resourceIdentifier, new AlertedResourceStatus(resourceIdentifier, alertSent, null, false));
            return this;
        }

        /**
         * Adds an alerted resource, that has successfully received the alert, to the
         * status object.
         * 
         * @param resourceIdentifier the identifier of the alerted resource, must not be
         *                           {@code null}.
         * @param alertSent          the instant at which the alert was sent to the
         *                           resource, must not be {@code null}.
         * @param alertDelivered     the instant at which the alert was successfully
         *                           delivered to the resource, must not be
         *                           {@code null}.
         * @return the builder itself.
         */
        public Builder withSuccessfullyAlertedResource(ResourceIdentifier resourceIdentifier, Instant alertSent,
                Instant alertDelivered) {
            requireNonNull(resourceIdentifier, "resourceIdentifier cannot be null");
            requireNonNull(alertSent, "alertSent cannot be null");
            requireNonNull(alertDelivered, "alertDelivered cannot be null");
            resources.put(resourceIdentifier,
                    new AlertedResourceStatus(resourceIdentifier, alertSent, alertDelivered, false));
            return this;
        }

        /**
         * Adds an alerted resource, that has not received the alert in time, to the
         * status object.
         * 
         * @param resourceIdentifier the identifier of the alerted resource, must not be
         *                           {@code null}.
         * @param alertSent          the instant at which the alert was sent to the
         *                           resource, must not be {@code null}.
         * @return the builder itself.
         */
        public Builder withTimedOutResource(ResourceIdentifier resourceIdentifier, Instant alertSent) {
            requireNonNull(resourceIdentifier, "resourceIdentifier cannot be null");
            requireNonNull(alertSent, "alertSent cannot be null");
            resources.put(resourceIdentifier, new AlertedResourceStatus(resourceIdentifier, alertSent, null, true));
            return this;
        }

        /**
         * Adds an alerted resource, that the system was unable to recognize and thus
         * could not alert at all, to the status object.
         * 
         * @param resourceIdentifier the identifier of the alerted resource, must not be
         *                           {@code null}.
         * @return the builder itself.
         */
        public Builder withUnknownResource(ResourceIdentifier resourceIdentifier) {
            requireNonNull(resourceIdentifier, "resourceIdentifier cannot be null");
            resources.put(resourceIdentifier, new AlertedResourceStatus(resourceIdentifier, null, null, false));
            return this;
        }

        /**
         * Builds a new {@link AlertStatus} object. This method can be invoked multiple
         * times and will yield a new instance every time.
         * 
         * @return a new {@link AlertStatus} object.
         */
        public AlertStatus build() {
            return new AlertStatus(this);
        }
    }

    /**
     * A data object describing the status of an alerted resource.
     */
    public static class AlertedResourceStatus {

        private final ResourceIdentifier resourceIdentifier;
        private final Instant alertSent;
        private final Instant alertDelivered;
        private final boolean timedOut;

        private AlertedResourceStatus(ResourceIdentifier resourceIdentifier, Instant alertSent, Instant alertDelivered,
                boolean timedOut) {
            this.resourceIdentifier = requireNonNull(resourceIdentifier);
            this.alertSent = alertSent;
            this.alertDelivered = alertDelivered;
            this.timedOut = timedOut;
        }

        /**
         * The identifier of the alerted resource.
         * 
         * @return the {@link ResourceIdentifier}, never {@code null}.
         */
        public ResourceIdentifier getResourceIdentifier() {
            return resourceIdentifier;
        }

        /**
         * The instant at which the alert was sent to the resource, provided that the
         * resource was known to the system.
         * 
         * @return an {@link Optional} containing the instant if the resource could be
         *         alerted.
         */
        public Optional<Instant> getAlertSent() {
            return Optional.ofNullable(alertSent);
        }

        /**
         * The instant at which the alert was successfully delivered to the resource.
         * 
         * @return an {@link Optional} containing the instant if the alert was
         *         successfully delivered.
         */
        public Optional<Instant> getAlertDelivered() {
            return Optional.ofNullable(alertDelivered);
        }

        /**
         * When the system alerts a resource, it needs to have at least one receiver to
         * send the alert to. When this method returns {@code true}, it means that the
         * alert has been sent to at least one receiver.
         * 
         * @return {@code true} if the system has managed to send the alert to the
         *         resource, {@code false} otherwise.
         */
        public boolean isAlertSent() {
            return alertSent != null;
        }

        /**
         * When the system has sent an alert to a receiver, it expects that receiver to
         * acknowledge that the alert has been delivered. When this method returns
         * {@code true}, it means that at least one receiver has acknowledged that the
         * alert has been delivered.
         * 
         * @return {@code true} if the system has managed to successfully deliver the
         *         alert to the resource, {@code false} otherwise.
         */
        public boolean isAlertDelivered() {
            return alertDelivered != null;
        }

        /**
         * When the system does not find any receivers for a resource, it considers the
         * resource to be unknown. When this method returns {@code true}, it means that
         * the system has not been able to find any receivers for the resource and thus
         * is not able to alert it at all.
         * 
         * @return {@code true} if the system did not recognize the resource,
         *         {@code false} otherwise.
         */
        public boolean isResourceUnknown() {
            return alertSent == null;
        }

        /**
         * When a receiver has not acknowledge that a sent alert has been delivered
         * within a specific time, it is considered timed out. When this method returns
         * {@code} true, the system has not received acknowledgment from any of the
         * receivers in time.
         * 
         * @return {@code true} if the system was not able to deliver the alert to the
         *         resource in time, {@code false} otherwise.
         */
        public boolean isTimedOut() {
            return timedOut;
        }
    }
}
