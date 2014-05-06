package net.pkhsolutions.idispatch.domain.status.events;

import net.pkhsolutions.idispatch.domain.status.ResourceStatus;
import org.springframework.context.ApplicationEvent;

/**
 * Event published when the state of a resource is changed.
 */
public class ResourceStatusChangedEvent extends ApplicationEvent {

    private final ResourceStatus resourceStatus;

    public ResourceStatusChangedEvent(Object source, ResourceStatus resourceStatus) {
        super(source);
        this.resourceStatus = resourceStatus;
    }

    public ResourceStatus getResourceStatus() {
        return resourceStatus;
    }
}
