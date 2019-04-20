package net.pkhapps.idispatch.alerter.server.rest.alert;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.ToString;

import java.util.Collection;

@Builder
@ToString
@JsonInclude(JsonInclude.Include.NON_EMPTY)
class SendAlertResponse {

    @JsonProperty
    Long alertId;

    @JsonProperty
    Collection<String> alertedResources;

    @JsonProperty
    Collection<String> unknownResources;
}
