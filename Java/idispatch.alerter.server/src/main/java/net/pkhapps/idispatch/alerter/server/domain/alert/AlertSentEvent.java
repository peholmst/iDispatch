package net.pkhapps.idispatch.alerter.server.domain.alert;

import lombok.Getter;
import net.pkhapps.idispatch.base.domain.DomainEvent;

import java.time.Instant;

import static java.util.Objects.requireNonNull;

/**
 * Domain event published when an alert is sent to one or more recipients.
 */
@Getter
public class AlertSentEvent implements DomainEvent {

    private final Alert alert;

    public AlertSentEvent(Alert alert) {
        this.alert = requireNonNull(alert);
    }

    @Override
    public Instant getOccurredOn() {
        return alert.getAlertDate();
    }
}
