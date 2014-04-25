package net.pkhsolutions.idispatch.domain.resources.events;

import net.pkhsolutions.idispatch.domain.resources.ResourceStateChange;
import org.springframework.context.ApplicationEvent;

/**
 * Event publish when the state of a resource is changed.
 */
public class ResourceStateChangedEvent extends ApplicationEvent {

    private final ResourceStateChange resourceStateChange;

    public ResourceStateChangedEvent(Object source, ResourceStateChange resourceStateChange) {
        super(source);
        this.resourceStateChange = resourceStateChange;
    }

    public ResourceStateChange getResourceStateChange() {
        return resourceStateChange;
    }
}
