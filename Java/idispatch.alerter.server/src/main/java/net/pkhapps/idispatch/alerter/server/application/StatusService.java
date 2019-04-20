package net.pkhapps.idispatch.alerter.server.application;

import net.pkhapps.idispatch.alerter.server.domain.alert.AlertId;
import net.pkhapps.idispatch.alerter.server.domain.recipient.RecipientId;
import net.pkhapps.idispatch.alerter.server.domain.recipient.ResourceCode;

import java.time.Instant;
import java.util.Optional;

public interface StatusService {

    interface AlertStatusResponse {

        AlertId getAlertId();

        Instant getAlertTime();

        boolean isAcknowledged(ResourceCode resourceCode);

        boolean isAcknowledged(RecipientId recipientId);

        Optional<Instant> getAcknowledgementTime(ResourceCode resourceCode);

        Optional<Instant> getAcknowledgementTime(RecipientId recipientId);

    }

}
