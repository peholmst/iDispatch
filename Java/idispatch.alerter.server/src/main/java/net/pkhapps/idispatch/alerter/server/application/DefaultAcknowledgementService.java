package net.pkhapps.idispatch.alerter.server.application;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.pkhapps.idispatch.alerter.server.domain.acknowledgement.Acknowledgement;
import net.pkhapps.idispatch.alerter.server.domain.acknowledgement.AcknowledgementRepository;
import net.pkhapps.idispatch.alerter.server.domain.alert.AlertId;
import net.pkhapps.idispatch.alerter.server.domain.alert.AlertRepository;
import net.pkhapps.idispatch.alerter.server.domain.recipient.RecipientId;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.Clock;

/**
 * Default implementation of {@link AcknowledgementService}.
 */
@Service
@Transactional(propagation = Propagation.REQUIRES_NEW)
// TODO Secure - recipients are only allowed to acknowledge their own alerts
@Slf4j
@AllArgsConstructor
class DefaultAcknowledgementService implements AcknowledgementService {

    private final AlertRepository alertRepository;
    private final AcknowledgementRepository acknowledgementRepository;
    private final Clock clock;

    @Override
    public void acknowledge(AlertId alert, RecipientId recipient) {
        if (isAlertSentToRecipient(alert, recipient)) {
            if (isAlertNotAcknowledgedYet(alert, recipient)) {
                var ack = new Acknowledgement(clock.instant(), alert, recipient);
                log.info("Recipient {} acknowledges alert {}", recipient, alert);
                acknowledgementRepository.saveAndFlush(ack);
            } else {
                log.info("Alert {} has already been acknowledged by recipient {}, ignoring", alert, recipient);
            }
        } else {
            log.warn("Recipient {} tried to acknowledge alert {} that was not sent to it", recipient, alert);
        }
    }

    private boolean isAlertSentToRecipient(AlertId alert, RecipientId recipient) {
        return alertRepository.findById(alert).filter(a -> a.getRecipients().contains(recipient)).isPresent();
    }

    private boolean isAlertNotAcknowledgedYet(AlertId alert, RecipientId recipient) {
        return !acknowledgementRepository.existsByRecipientAndAlert(recipient, alert);
    }
}
