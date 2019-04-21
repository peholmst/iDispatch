package net.pkhapps.idispatch.alerter.server.adapter.rest.alert;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.ToString;

import java.time.Instant;
import java.util.Map;

@Builder
@ToString
@JsonInclude(JsonInclude.Include.NON_EMPTY)
class AlertStatusResponse {

    @JsonProperty
    Long alertId;

    @JsonProperty
    Instant alertDate;

    @JsonProperty
    Map<String, Instant> ackResources;

    @JsonProperty
    @ToString.Include
    int ackResourcesCount() {
        return ackResources.size();
    }

    @JsonProperty
    Map<Long, Instant> ackRecipients;

    @JsonProperty
    @ToString.Include
    int ackRecipientsCount() {
        return ackRecipients.size();
    }
}
