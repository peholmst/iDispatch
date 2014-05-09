package net.pkhsolutions.idispatch.ui.dws.assignments;

import com.vaadin.data.Property;
import com.vaadin.ui.UI;
import net.pkhsolutions.idispatch.boundary.ResourceStatusService;
import net.pkhsolutions.idispatch.boundary.events.ResourceStatusChanged;
import net.pkhsolutions.idispatch.entity.Assignment;
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
import java.util.Objects;

import static java.util.Collections.emptyList;

/**
 * Container for the current status of all resources assigned to a {@link net.pkhsolutions.idispatch.entity.Assignment}.
 */
@Component
@Scope("prototype")
class AssignedResourcesContainer extends AbstractResourceStatusContainer implements UIAttachable {

    @Autowired
    ResourceStatusService resourceStatusService;
    @Autowired
    @EventBusScope(EventScope.APPLICATION)
    EventBus eventBus;
    @Autowired
    DwsUI ui;

    private Assignment assignment;

    void setAssignmentModel(AssignmentModel assignmentModel) {
        assignment = assignmentModel.assignment().getValue();
        ((Property.ValueChangeNotifier) assignmentModel.assignment()).addValueChangeListener(event -> {
            final Assignment newAssignment = (Assignment) event.getProperty().getValue();
            if (!Objects.equals(assignment, newAssignment)) {
                assignment = newAssignment;
                refresh();
            }
        });
    }

    @Override
    protected List<ResourceStatus> doRefresh() {
        if (assignment == null) {
            return emptyList();
        } else {
            return resourceStatusService.getStatusOfResourcesAssignedToAssignment(assignment);
        }
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
