package net.pkhapps.idispatch.alerter.server.application;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.pkhapps.idispatch.alerter.server.domain.acknowledgement.Acknowledgement;
import net.pkhapps.idispatch.alerter.server.domain.acknowledgement.AcknowledgementRepository;
import net.pkhapps.idispatch.alerter.server.domain.alert.Alert;
import net.pkhapps.idispatch.alerter.server.domain.alert.AlertId;
import net.pkhapps.idispatch.alerter.server.domain.alert.AlertRepository;
import net.pkhapps.idispatch.alerter.server.domain.recipient.Recipient;
import net.pkhapps.idispatch.alerter.server.domain.recipient.RecipientId;
import net.pkhapps.idispatch.alerter.server.domain.recipient.RecipientRepository;
import net.pkhapps.idispatch.alerter.server.domain.recipient.ResourceCode;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Default implementation of {@link StatusService}.
 */
@Service
@Transactional(propagation = Propagation.REQUIRES_NEW)
// TODO Secure - only users with permission to check alert status should be given access
@Slf4j
@AllArgsConstructor
class DefaultStatusService implements StatusService {

    private static final int ACKNOWLEDGEMENT_PAGE_SIZE = 1000;

    private final AlertRepository alertRepository;
    private final AcknowledgementRepository acknowledgementRepository;
    private final RecipientRepository recipientRepository;

    @Override
    public Optional<AlertStatusResponse> getAlertStatus(AlertId alertId) {
        log.debug("Looking up alert with ID {}", alertId);
        return alertRepository.findById(alertId).map(this::toResponse);
    }

    private AlertStatusResponse toResponse(Alert alert) {
        var acks = acknowledgementRepository.findByAlert(alert.getId(), PageRequest.of(0, ACKNOWLEDGEMENT_PAGE_SIZE));
        if (acks.hasNext()) {
            // This should never happen but we include an upper limit (the page size) to avoid crashing the app in case
            // the query for some strange reason would return a huge amount of objects
            log.error("Found more than {} acknowledgements for alert {}. Please investigate!", ACKNOWLEDGEMENT_PAGE_SIZE, alert.getId());
        }
        var recipientAckDates = acks.get().collect(Collectors.toMap(Acknowledgement::getRecipient, Acknowledgement::getAcknowledgementDate));
        var recipients = recipientRepository.findByIds(recipientAckDates.keySet().stream());
        var resourceAckDates = computeResourceAcknowledgementDates(recipients, recipientAckDates);
        return new AlertStatusResponseImpl(alert, recipientAckDates, resourceAckDates);
    }

    private Map<ResourceCode, Instant> computeResourceAcknowledgementDates(List<Recipient<?>> recipients,
                                                                           Map<RecipientId, Instant> recipientAcknowledgementDates) {
        var ackDates = new HashMap<ResourceCode, Instant>();

        recipients.forEach(recipient -> {
            var ackDate = recipientAcknowledgementDates.get(recipient.getId());
            if (ackDate != null) {
                recipient.getResources().forEach(resourceCode -> setResourceAcknowledgementDate(resourceCode, ackDate, ackDates));
            }
        });

        return ackDates;
    }

    private void setResourceAcknowledgementDate(ResourceCode resourceCode, Instant candidate,
                                                Map<ResourceCode, Instant> destination) {
        var existing = destination.get(resourceCode);
        if (existing == null || existing.isAfter(candidate)) {
            destination.put(resourceCode, candidate);
        }
    }

    private static class AlertStatusResponseImpl implements AlertStatusResponse {

        private final AlertId alertId;
        private final Instant alertDate;
        private final Collection<RecipientId> recipients;
        private final Collection<ResourceCode> resources;
        private final Map<RecipientId, Instant> recipientAckDates;
        private final Map<ResourceCode, Instant> resourceAckDates;

        private AlertStatusResponseImpl(Alert alert, Map<RecipientId, Instant> recipientAckDates,
                                        Map<ResourceCode, Instant> resourceAckDates) {
            alertId = alert.getId();
            alertDate = alert.getAlertDate();
            recipients = Set.copyOf(alert.getRecipients());
            resources = Set.copyOf(alert.getResources());
            this.recipientAckDates = Collections.unmodifiableMap(recipientAckDates);
            this.resourceAckDates = Collections.unmodifiableMap(resourceAckDates);
        }

        @Override
        public AlertId getAlertId() {
            return alertId;
        }

        @Override
        public Instant getAlertDate() {
            return alertDate;
        }

        @Override
        public Optional<Instant> getAcknowledgementDate(ResourceCode resourceCode) {
            return Optional.ofNullable(resourceAckDates.get(resourceCode));
        }

        @Override
        public Optional<Instant> getAcknowledgementDate(RecipientId recipientId) {
            return Optional.ofNullable(recipientAckDates.get(recipientId));
        }

        @Override
        public Collection<RecipientId> getRecipients() {
            return recipients;
        }

        @Override
        public Collection<ResourceCode> getResources() {
            return resources;
        }
    }
}
