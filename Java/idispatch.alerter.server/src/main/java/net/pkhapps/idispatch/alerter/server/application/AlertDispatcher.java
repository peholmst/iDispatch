package net.pkhapps.idispatch.alerter.server.application;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.pkhapps.idispatch.alerter.server.domain.alert.Alert;
import net.pkhapps.idispatch.alerter.server.domain.alert.AlertSentEvent;
import net.pkhapps.idispatch.alerter.server.domain.recipient.Recipient;
import net.pkhapps.idispatch.alerter.server.domain.recipient.RecipientRepository;
import org.springframework.context.ApplicationContext;
import org.springframework.lang.Nullable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import java.util.Comparator;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/**
 * The alert dispatcher is responsible for dispatching an alert to its recipients.
 */
@Service
@Slf4j
@AllArgsConstructor
class AlertDispatcher {

    private final RecipientRepository recipientRepository;
    private final Map<Class<? extends Recipient>, SingleRecipientAlertDispatcher> dispatcherCache = new ConcurrentHashMap<>();
    private final ApplicationContext applicationContext;

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    @Async
    public void onAlertSentEvent(AlertSentEvent alertSentEvent) {
        log.debug("Dispatching alert {}", alertSentEvent.getAlert());
        var recipients = recipientRepository.findByIds(alertSentEvent.getAlert().getRecipients().stream());
        recipients.sort(Comparator.comparing(Recipient::getPriority));
        recipients.forEach(r -> dispatchAlert(alertSentEvent.getAlert(), r));
    }

    private void dispatchAlert(Alert alert, Recipient<?> recipient) {
        log.debug("Dispatching {} to recipient {} with priority {}", alert, recipient, recipient.getPriority());
        findDispatcher(recipient.getClass()).ifPresent(dispatcher -> dispatcher.dispatch(alert, recipient));
    }

    private Optional<SingleRecipientAlertDispatcher> findDispatcher(Class<? extends Recipient> recipientClass) {
        return Optional.ofNullable(dispatcherCache.computeIfAbsent(recipientClass, this::findDispatcherFromApplicationContext));
    }

    @Nullable
    private SingleRecipientAlertDispatcher findDispatcherFromApplicationContext(Class<? extends Recipient> recipientClass) {
        var dispatcher = applicationContext.getBeansOfType(SingleRecipientAlertDispatcher.class).values().stream()
                .filter(d -> d.supports(recipientClass))
                .findFirst()
                .orElse(null);
        if (dispatcher == null) {
            log.error("Found no dispatcher for {}", recipientClass);
        } else {
            log.debug("Found dispatcher {} for {}", dispatcher, recipientClass);
        }
        return dispatcher;
    }
}
