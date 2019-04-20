package net.pkhapps.idispatch.alerter.server.application;

import net.pkhapps.idispatch.alerter.server.domain.alert.AlertId;
import net.pkhapps.idispatch.alerter.server.domain.recipient.RecipientId;
import net.pkhapps.idispatch.alerter.server.domain.recipient.ResourceCode;

import java.time.Instant;
import java.util.Collection;
import java.util.Optional;

/**
 * Application service for checking the status of alerts.
 */
public interface StatusService {

    /**
     * Retrieves the current status of the given alert.
     *
     * @param alertId the ID of the alert.
     * @return a response object with information about the current status of the alert or an empty {@code Optional} if
     * the alert did not exist.
     */
    Optional<AlertStatusResponse> getAlertStatus(AlertId alertId);

    /**
     * Interface defining the response of the {@link #getAlertStatus(AlertId)} method.
     */
    interface AlertStatusResponse {

        /**
         * Returns the ID of the alert.
         */
        AlertId getAlertId();

        /**
         * Returns the date and time at which the alert was sent.
         */
        Instant getAlertDate();

        /**
         * Returns whether the alert has been acknowledged by the given resource.
         */
        default boolean isAcknowledged(ResourceCode resourceCode) {
            return getAcknowledgementDate(resourceCode).isPresent();
        }

        /**
         * Returns whether the alert has been acknowledged by the given recipient.
         */
        default boolean isAcknowledged(RecipientId recipientId) {
            return getAcknowledgementDate(recipientId).isPresent();
        }

        /**
         * Returns the date and time at which the given resource acknowledged the alert.
         */
        Optional<Instant> getAcknowledgementDate(ResourceCode resourceCode);

        /**
         * Returns the date and time at which the given recipient acknowledged the alert.
         */
        Optional<Instant> getAcknowledgementDate(RecipientId recipientId);

        /**
         * Returns the recipients that the alert was sent to.
         */
        Collection<RecipientId> getRecipients();

        /**
         * Returns the resources that the alert was sent to.
         */
        Collection<ResourceCode> getResources();

        /**
         * Returns whether there are any recipients that have not yet acknowledged the alert.
         */
        default boolean hasUnacknowledgedRecipients() {
            return getRecipients().stream().anyMatch(recipientId -> !isAcknowledged(recipientId));
        }

        /**
         * Returns whether there are any resources that have not yet acknowledged the alert.
         */
        default boolean hasUnacknowledgedResources() {
            return getResources().stream().anyMatch(resourceCode -> !isAcknowledged(resourceCode));
        }
    }
}
