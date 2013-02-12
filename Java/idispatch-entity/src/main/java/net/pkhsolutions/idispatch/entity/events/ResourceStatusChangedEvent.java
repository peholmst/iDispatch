package net.pkhsolutions.idispatch.entity.events;

import net.pkhsolutions.idispatch.entity.ResourceStatus;

import java.io.Serializable;

public class ResourceStatusChangedEvent implements Serializable {

    private final ResourceStatus resourceStatus;

    public ResourceStatusChangedEvent(ResourceStatus resourceStatus) {
        this.resourceStatus = resourceStatus;
    }

    public ResourceStatus getResourceStatus() {
        return resourceStatus;
    }
}
