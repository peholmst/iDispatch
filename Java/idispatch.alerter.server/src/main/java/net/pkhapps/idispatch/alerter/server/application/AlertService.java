package net.pkhapps.idispatch.alerter.server.application;

import com.fasterxml.jackson.databind.JsonNode;
import net.pkhapps.idispatch.alerter.server.domain.alert.AlertId;
import net.pkhapps.idispatch.alerter.server.domain.alert.AlertPriority;
import net.pkhapps.idispatch.alerter.server.domain.recipient.RecipientId;
import net.pkhapps.idispatch.alerter.server.domain.recipient.ResourceCode;

import java.util.Collection;
import java.util.Optional;

/**
 * Application service for sending alerts.
 */
public interface AlertService {

    /**
     * Sends an alert to the given resources.
     *
     * @param priority    the priority of the alert.
     * @param contentType the content type of the alert. The recipients are assumed to know what this means.
     * @param content     the content of the alert. The recipients are assumed to be able to decode this.
     * @param resources   the resources that will receive the alert (at least one).
     * @return a response object with information about the alert.
     */
    ResourceAlertResponse sendAlertToResources(AlertPriority priority, String contentType, JsonNode content,
                                               Collection<ResourceCode> resources);

    /**
     * Interface defining the response of the {@link #sendAlertToResources(AlertPriority, String, JsonNode, Collection)}
     * method.
     */
    interface ResourceAlertResponse {

        /**
         * Returns whether an alert was sent to at least some of the resources.
         */
        default boolean resourcesWereAlerted() {
            return getAlertId().isPresent();
        }

        /**
         * Returns the ID of the alert that was created, if one was created.
         *
         * @see #getAlertedResources()
         * @see #getResourcesWithoutRecipients()
         */
        Optional<AlertId> getAlertId();

        /**
         * Returns the recipients that were alerted for the given resource.
         */
        Collection<RecipientId> getRecipientsForResource(ResourceCode resource);

        /**
         * Returns the resources that were alerted. If this collection is empty then no alert was created at all.
         */
        Collection<ResourceCode> getAlertedResources();

        /**
         * Returns the resources that could not be alerted because there were not any recipients defined for them.
         */
        Collection<ResourceCode> getResourcesWithoutRecipients();
    }
}
