package net.pkhapps.idispatch.alerter.server.domain.acknowledgement;

import lombok.Getter;
import net.pkhapps.idispatch.base.domain.DomainEvent;

import java.time.Instant;

import static java.util.Objects.requireNonNull;

/**
 * Domain event published when an alert is acknowledged by a recipient.
 */
@Getter
public class AlertAcknowledgedEvent implements DomainEvent {

    private final Acknowledgement acknowledgement;

    public AlertAcknowledgedEvent(Acknowledgement acknowledgement) {
        this.acknowledgement = requireNonNull(acknowledgement);
    }

    @Override
    public Instant getOccurredOn() {
        return acknowledgement.getAcknowledgementDate();
    }
}
