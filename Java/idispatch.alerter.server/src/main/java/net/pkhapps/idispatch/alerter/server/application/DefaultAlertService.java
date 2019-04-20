package net.pkhapps.idispatch.alerter.server.application;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.pkhapps.idispatch.alerter.server.domain.alert.Alert;
import net.pkhapps.idispatch.alerter.server.domain.alert.AlertId;
import net.pkhapps.idispatch.alerter.server.domain.alert.AlertPriority;
import net.pkhapps.idispatch.alerter.server.domain.alert.AlertRepository;
import net.pkhapps.idispatch.alerter.server.domain.recipient.Recipient;
import net.pkhapps.idispatch.alerter.server.domain.recipient.RecipientId;
import net.pkhapps.idispatch.alerter.server.domain.recipient.RecipientRepository;
import net.pkhapps.idispatch.alerter.server.domain.recipient.ResourceCode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.Clock;
import java.util.*;
import java.util.stream.Collectors;

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
    private final RecipientRepository recipientRepository;
    private final Clock clock;

    @Override
    public ResourceAlertResponse sendAlertToResources(AlertPriority priority, String contentType, JsonNode content, Collection<ResourceCode> resources) {
        log.debug("Looking up recipients for resources {}", resources);
        final var resourcesWithoutRecipients = new HashSet<ResourceCode>();
        final var recipients = new HashMap<ResourceCode, Collection<RecipientId>>();
        resources.forEach(resourceCode -> {
            var recipientsForResource = recipientRepository.findByResource(resourceCode).map(Recipient::getId).collect(Collectors.toSet());
            if (recipientsForResource.size() > 0) {
                recipients.put(resourceCode, recipientsForResource);
            } else {
                resourcesWithoutRecipients.add(resourceCode);
            }
        });

        if (recipients.isEmpty()) {
            log.warn("Found no recipients for resources {}, cannot send alert", resources);
            return new ResourceAlertResponseImpl(resourcesWithoutRecipients);
        } else {
            log.debug("Sending alert with priority [{}] and content type [{}] to {}", priority, contentType, recipients);
            var alert = alertRepository.saveAndFlush(new Alert(priority, clock.instant(), contentType, content, recipients));
            return new ResourceAlertResponseImpl(alert.getId(), recipients, resourcesWithoutRecipients);
        }
    }

    private static class ResourceAlertResponseImpl implements ResourceAlertResponse {

        private final AlertId alertId;
        private final Map<ResourceCode, Collection<RecipientId>> recipients;
        private final Collection<ResourceCode> resourcesWithoutRecipients;

        private ResourceAlertResponseImpl(Set<ResourceCode> resourcesWithoutRecipients) {
            alertId = null;
            recipients = Collections.emptyMap();
            this.resourcesWithoutRecipients = Collections.unmodifiableSet(resourcesWithoutRecipients);
        }

        private ResourceAlertResponseImpl(AlertId alertId, Map<ResourceCode, Collection<RecipientId>> recipients,
                                          Set<ResourceCode> resourcesWithoutRecipients) {
            this.alertId = alertId;
            this.recipients = Collections.unmodifiableMap(recipients);
            this.resourcesWithoutRecipients = Collections.unmodifiableSet(resourcesWithoutRecipients);
        }

        @Override
        public Optional<AlertId> getAlertId() {
            return Optional.ofNullable(alertId);
        }

        @Override
        public Collection<RecipientId> getRecipientsForResource(ResourceCode resource) {
            return Optional.ofNullable(recipients.get(resource)).orElseGet(Collections::emptySet);
        }

        @Override
        public Collection<ResourceCode> getAlertedResources() {
            return recipients.keySet();
        }

        @Override
        public Collection<ResourceCode> getResourcesWithoutRecipients() {
            return resourcesWithoutRecipients;
        }
    }
}
