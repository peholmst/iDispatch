package net.pkhapps.idispatch.infra.api.devices.data;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.Instant;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class BriefDeviceDescriptor {

    @JsonProperty
    public String deviceId;

    @JsonProperty
    public String description;

    @JsonProperty
    public boolean enabled;

    @JsonProperty
    public boolean inUse;

    @JsonProperty
    public Instant lastSeen;
}
