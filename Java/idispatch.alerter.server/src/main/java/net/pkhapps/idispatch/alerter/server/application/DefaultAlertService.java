package net.pkhapps.idispatch.alerter.server.application;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.pkhapps.idispatch.alerter.server.domain.alert.Alert;
import net.pkhapps.idispatch.alerter.server.domain.alert.AlertId;
import net.pkhapps.idispatch.alerter.server.domain.alert.AlertPriority;
import net.pkhapps.idispatch.alerter.server.domain.alert.AlertRepository;
import net.pkhapps.idispatch.alerter.server.domain.recipient.RecipientId;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.Clock;
import java.util.Collection;

/**
 * Default implementation of {@link AlertService}.
 */
@Service
@Transactional(propagation = Propagation.REQUIRES_NEW)
// TODO Secure - only users with permission to send alerts should be given access
@Slf4j
@AllArgsConstructor
class DefaultAlertService implements AlertService {

    private final AlertRepository alertRepository;
    private final Clock clock;

    @Override
    public AlertId sendAlert(AlertPriority priority, String contentType, JsonNode content, Collection<RecipientId> recipients) {
        var alert = new Alert(priority, clock.instant(), contentType, content, recipients);
        log.info("Sending alert with priority [{}] and content type [{}] to [{}]", priority, contentType, recipients);
        return alertRepository.saveAndFlush(alert).getId();
    }
}
