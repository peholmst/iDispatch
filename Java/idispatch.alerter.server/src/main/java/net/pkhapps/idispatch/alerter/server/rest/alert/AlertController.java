package net.pkhapps.idispatch.alerter.server.rest.alert;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.pkhapps.idispatch.alerter.server.application.AcknowledgementService;
import net.pkhapps.idispatch.alerter.server.application.AlertService;
import net.pkhapps.idispatch.alerter.server.application.StatusService;
import net.pkhapps.idispatch.alerter.server.domain.alert.AlertId;
import net.pkhapps.idispatch.alerter.server.domain.alert.AlertPriority;
import net.pkhapps.idispatch.alerter.server.domain.recipient.RecipientId;
import net.pkhapps.idispatch.alerter.server.domain.recipient.ResourceCode;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.time.Instant;
import java.util.HashMap;
import java.util.stream.Collectors;

@RestController
@AllArgsConstructor
@Slf4j
@RequestMapping("/api/alert")
@Validated
class AlertController {

    private final AlertService alertService;
    private final StatusService statusService;
    private final AcknowledgementService acknowledgementService;

    @PostMapping(value = "/send", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<SendAlertResponse> sendAlert(@Valid @RequestBody SendAlertCommand command) {
        log.debug("Received {}", command);

        final var priority = AlertPriority.values()[command.getPriority()];
        final var resources = command.getResources().stream().map(ResourceCode::new).collect(Collectors.toSet());
        final var response = alertService.sendAlertToResources(priority, command.getContentType(),
                command.getContent(), resources);

        var jsonResponse = SendAlertResponse.builder()
                .unknownResources(response.getResourcesWithoutRecipients()
                        .stream().map(ResourceCode::toString).collect(Collectors.toList()))
                .alertedResources(response.getAlertedResources()
                        .stream().map(ResourceCode::toString).collect(Collectors.toList()))
                .alertId(response.getAlertId().map(AlertId::toLong).orElse(null))
                .build();
        log.debug("Responding {}", jsonResponse);
        return ResponseEntity.ok(jsonResponse);
    }

    @GetMapping("/status/{alertId}")
    public ResponseEntity<AlertStatusResponse> getAlertStatus(@PathVariable("alertId") @NotNull Long alertId) {
        log.debug("Received status request for alert {}", alertId);

        var status = statusService.getAlertStatus(new AlertId(alertId));
        if (status.isPresent()) {
            var jsonResponse = toJsonResponse(status.get());
            log.debug("Responding {}", jsonResponse);
            return ResponseEntity.ok(jsonResponse);
        } else {
            log.debug("Found no alert with ID {}", alertId);
            return ResponseEntity.notFound().build();
        }
    }

    private AlertStatusResponse toJsonResponse(StatusService.AlertStatusResponse response) {
        var ackResources = new HashMap<String, Instant>();
        response.getResources().forEach(resourceCode -> response.getAcknowledgementDate(resourceCode).ifPresent(date -> ackResources.put(resourceCode.toString(), date)));
        var ackRecipients = new HashMap<Long, Instant>();
        response.getRecipients().forEach(recipientId -> response.getAcknowledgementDate(recipientId).ifPresent(date -> ackRecipients.put(recipientId.toLong(), date)));
        return AlertStatusResponse.builder()
                .alertId(response.getAlertId().toLong())
                .alertDate(response.getAlertDate())
                .ackRecipients(ackRecipients)
                .ackResources(ackResources)
                .build();
    }

    @PutMapping(path = "/ack/{alertId}", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ResponseEntity<Void> acknowledge(@PathVariable("alertId") @NotNull Long alertId,
                                            @RequestParam("recipientId") @NotNull Long recipientId) {
        log.debug("Received acknowledgment of alert {} from recipient {}", alertId, recipientId);
        acknowledgementService.acknowledge(new AlertId(alertId), new RecipientId(recipientId));
        return ResponseEntity.ok().build();
    }
}
