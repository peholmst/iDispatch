package net.pkhsolutions.idispatch.ui.dws.assignments;

import com.vaadin.ui.UI;
import net.pkhsolutions.idispatch.boundary.ResourceStatusService;
import net.pkhsolutions.idispatch.boundary.events.ResourceStatusChanged;
import net.pkhsolutions.idispatch.entity.ResourceStatus;
import net.pkhsolutions.idispatch.ui.common.UIAttachable;
import net.pkhsolutions.idispatch.ui.dws.DwsUI;
import net.pkhsolutions.idispatch.ui.dws.resources.AbstractResourceStatusContainer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.vaadin.spring.events.EventBus;
import org.vaadin.spring.events.EventBusListenerMethod;
import org.vaadin.spring.events.EventBusScope;
import org.vaadin.spring.events.EventScope;

import java.util.List;

/**
 * Container for the current status of all assignable resources.
 */
@Component
@Scope("prototype")
class AssignableResourcesContainer extends AbstractResourceStatusContainer implements UIAttachable {

    @Autowired
    ResourceStatusService resourceStatusService;
    @Autowired
    @EventBusScope(EventScope.APPLICATION)
    EventBus eventBus;
    @Autowired
    DwsUI ui;

    @Override
    protected List<ResourceStatus> doRefresh() {
        return resourceStatusService.getStatusOfAvailableResources();
    }

    @EventBusListenerMethod
    void onResourceStatusChangedEvent(ResourceStatusChanged event) {
        logger.debug("Resource status changed event received, refreshing container");
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
