package net.pkhsolutions.idispatch.ui.dws.resources;

import com.vaadin.ui.UI;
import net.pkhsolutions.idispatch.boundary.ResourceStatusService;
import net.pkhsolutions.idispatch.boundary.events.AssignmentEvent;
import net.pkhsolutions.idispatch.boundary.events.ResourceStatusChanged;
import net.pkhsolutions.idispatch.entity.ResourceStatus;
import net.pkhsolutions.idispatch.ui.common.UIAttachable;
import net.pkhsolutions.idispatch.ui.dws.DwsUI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.vaadin.spring.events.EventBus;
import org.vaadin.spring.events.EventBusListenerMethod;
import org.vaadin.spring.events.EventBusScope;
import org.vaadin.spring.events.EventScope;

import java.util.List;

/**
 * Container for the current status of all active resources.
 */
@Component
@Scope("prototype")
class ResourceStatusContainer extends AbstractResourceStatusContainer implements UIAttachable {

    @Autowired
    ResourceStatusService resourceStatusService;
    @Autowired
    @EventBusScope(EventScope.APPLICATION)
    EventBus eventBus;
    @Autowired
    DwsUI ui;

    @Override
    protected List<ResourceStatus> doRefresh() {
        return resourceStatusService.getStatusOfAllResources();
    }

    @EventBusListenerMethod
    void onTicketEvent(AssignmentEvent event) {
        ui.access(this::refresh);
    }

    @EventBusListenerMethod
    void onResourceStatusChangedEvent(ResourceStatusChanged event) {
        ui.access(this::refresh);
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
