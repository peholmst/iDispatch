package net.pkhapps.idispatch.alerter.server.application;

import net.pkhapps.idispatch.alerter.server.domain.alert.AlertId;
import net.pkhapps.idispatch.alerter.server.domain.recipient.RecipientId;

/**
 * Application service for acknowledging alerts.
 */
public interface AcknowledgementService {

    /**
     * Acknowledges that the given recipient has received the given alert. If more than one acknowledgment is made only
     * the first will be recorded and the rest will be ignored.
     *
     * @param alert     the alert to acknowledge.
     * @param recipient the recipient that is making the acknowledgment.
     */
    void acknowledge(AlertId alert, RecipientId recipient);
}
