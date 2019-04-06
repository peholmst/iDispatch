package net.pkhapps.idispatch.alerter.server.domain.recipient;

import net.pkhapps.idispatch.base.domain.OrganizationId;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

/**
 * Recipient that receives alerts through STOMP (websocket).
 */
@Entity
@DiscriminatorValue("STOMP")
public class StompRecipient extends Recipient<StompRecipient> {

    protected StompRecipient() {
    }

    public StompRecipient(String description, OrganizationId organizationId) {
        super(description, organizationId);
    }
}
