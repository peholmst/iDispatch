package net.pkhapps.idispatch.alerter.server.rest.alert;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.Getter;
import lombok.ToString;

import javax.validation.constraints.*;
import java.util.List;

@Getter
@ToString
class SendAlertCommand {

    @JsonProperty
    @Min(0)
    @Max(2)
    private int priority;

    @JsonProperty
    @NotBlank
    private String contentType;

    @JsonProperty
    @NotNull
    private JsonNode content;

    @JsonProperty
    @NotEmpty
    private List<String> resources;
}
