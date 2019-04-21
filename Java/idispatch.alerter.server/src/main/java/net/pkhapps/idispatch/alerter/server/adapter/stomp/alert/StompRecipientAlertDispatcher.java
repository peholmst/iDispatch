package net.pkhapps.idispatch.alerter.server.adapter.stomp.alert;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.pkhapps.idispatch.alerter.server.application.SingleRecipientAlertDispatcher;
import net.pkhapps.idispatch.alerter.server.domain.alert.Alert;
import net.pkhapps.idispatch.alerter.server.domain.recipient.Recipient;
import net.pkhapps.idispatch.alerter.server.domain.recipient.StompRecipient;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
@Slf4j
class StompRecipientAlertDispatcher implements SingleRecipientAlertDispatcher {

    private final SimpMessagingTemplate simpMessagingTemplate;
    private final ObjectMapper objectMapper;

    private ExecutorService executorService;

    @PostConstruct
    void init() {
        log.info("Starting StompRecipient dispatcher thread");
        // TODO Make the thread pool executor and queue parameters configurable
        executorService = new ThreadPoolExecutor(1, 1, 0, TimeUnit.SECONDS,
                new LinkedBlockingQueue<>(100));
    }

    @PreDestroy
    void destroy() {
        log.info("Shutting down StompRecipient dispatcher thread");
        executorService.shutdown();
    }

    @Override
    public boolean supports(Class<? extends Recipient> recipientClass) {
        return StompRecipient.class.isAssignableFrom(recipientClass);
    }

    @Override
    public void dispatch(Alert alert, Recipient<?> recipient) {
        executorService.submit(() -> doDispatch(alert, recipient));
    }

    private void doDispatch(Alert alert, Recipient<?> recipient) {
        try {
            var headers = Map.of("id", (Object) alert.getNonNullId().toString(),
                    "contentType", alert.getContentType(),
                    "priority", alert.getPriority().ordinal());
            var payload = objectMapper.writeValueAsString(alert.getContent());
            var destination = "/alerts/" + recipient.getNonNullId();
            log.debug("Sending [{}] to destination [{}] with headers {}", payload, destination, headers);
            simpMessagingTemplate.convertAndSend(destination, payload, headers);
        } catch (Exception ex) {
            log.error("Error dispatching alert " + alert + " to " + recipient, ex);
        }
    }
}
