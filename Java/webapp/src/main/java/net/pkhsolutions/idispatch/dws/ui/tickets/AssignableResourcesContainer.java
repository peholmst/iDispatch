package net.pkhsolutions.idispatch.dws.ui.tickets;

import net.pkhsolutions.idispatch.domain.resources.ResourceService;
import net.pkhsolutions.idispatch.domain.resources.ResourceStatus;
import net.pkhsolutions.idispatch.domain.resources.events.ResourceStatusChangedEvent;
import net.pkhsolutions.idispatch.dws.ui.resources.AbstractResourceStatusContainer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.vaadin.spring.events.EventBusListenerMethod;

import java.util.List;

/**
 * Container for the current status of all assignable resources. Remember to subscribe the container to the application
 * scoped {@link org.vaadin.spring.events.EventBus}.
 */
@Component
@Scope("prototype")
public class AssignableResourcesContainer extends AbstractResourceStatusContainer {

    @Autowired
    ResourceService resourceService;

    @Override
    protected List<ResourceStatus> doRefresh() {
        return resourceService.getCurrentStatusOfActiveAssignableResources();
    }

    @EventBusListenerMethod
    void onResourceStatusChangedEvent(ResourceStatusChangedEvent event) {
        refresh();
    }
}
