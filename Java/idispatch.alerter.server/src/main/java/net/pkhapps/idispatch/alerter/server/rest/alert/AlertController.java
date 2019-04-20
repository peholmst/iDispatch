package net.pkhapps.idispatch.alerter.server.rest.alert;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.pkhapps.idispatch.alerter.server.application.AlertService;
import net.pkhapps.idispatch.alerter.server.domain.alert.AlertId;
import net.pkhapps.idispatch.alerter.server.domain.alert.AlertPriority;
import net.pkhapps.idispatch.alerter.server.domain.recipient.ResourceCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.stream.Collectors;

@RestController
@AllArgsConstructor
@Slf4j
@RequestMapping("/api/alert")
@Validated
class AlertController {

    private final AlertService alertService;

    @PostMapping("/send")
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

        return ResponseEntity.notFound().build();
    }
}
