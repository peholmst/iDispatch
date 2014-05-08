package net.pkhsolutions.idispatch.ui.dws.resources;

import com.vaadin.ui.UI;
import net.pkhsolutions.idispatch.boundary.ResourceStatusService;
import net.pkhsolutions.idispatch.boundary.events.AssignmentEvent;
import net.pkhsolutions.idispatch.boundary.events.ResourceStatusChanged;
import net.pkhsolutions.idispatch.entity.ResourceStatus;
import net.pkhsolutions.idispatch.ui.common.UIAttachable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.vaadin.spring.events.EventBus;
import org.vaadin.spring.events.EventBusListenerMethod;

import java.util.List;

/**
 * Container for the current status of all active resources.
 */
@Component
@Scope("prototype")
public class ResourceStatusContainer extends AbstractResourceStatusContainer implements UIAttachable {

    @Autowired
    ResourceStatusService resourceStatusService;
    @Autowired
    EventBus eventBus;

    @Override
    protected List<ResourceStatus> doRefresh() {
        return resourceStatusService.getStatusOfAllResources();
    }

    @EventBusListenerMethod
    void onTicketEvent(AssignmentEvent event) {
        refresh();
    }

    @EventBusListenerMethod
    void onResourceStatusChangedEvent(ResourceStatusChanged event) {
        refresh();
    }

    @Override
    public void attachedToUI(UI ui) {
        eventBus.subscribe(this);
    }

    @Override
    public void detachedFromUI(UI ui) {
        eventBus.unsubscribe(this);
    }
}
