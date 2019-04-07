package net.pkhapps.idispatch.alerter.server.application;

import com.fasterxml.jackson.databind.JsonNode;
import net.pkhapps.idispatch.alerter.server.domain.alert.AlertId;
import net.pkhapps.idispatch.alerter.server.domain.alert.AlertPriority;
import net.pkhapps.idispatch.alerter.server.domain.recipient.RecipientId;

import java.util.Collection;

/**
 * Application service for sending alerts.
 */
public interface AlertService {

    /**
     * Sends an alert to the given recipients.
     *
     * @param priority    the priority of the alert.
     * @param contentType the content type of the alert. The recipients are assumed to know what this means.
     * @param content     the content of the alert. The recipients are assumed to be able to decode this.
     * @param recipients  the recipients of the alert (at least one).
     * @return the ID of the sent alert.
     */
    AlertId sendAlert(AlertPriority priority, String contentType, JsonNode content, Collection<RecipientId> recipients);
}
