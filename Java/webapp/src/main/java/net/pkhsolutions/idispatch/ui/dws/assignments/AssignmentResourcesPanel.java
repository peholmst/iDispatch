package net.pkhsolutions.idispatch.ui.dws.assignments;

import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.HorizontalSplitPanel;
import com.vaadin.ui.VerticalLayout;
import net.pkhsolutions.idispatch.boundary.ResourceStatusService;
import net.pkhsolutions.idispatch.entity.Assignment;
import net.pkhsolutions.idispatch.entity.Resource;
import net.pkhsolutions.idispatch.entity.ResourceStatus;
import net.pkhsolutions.idispatch.ui.dws.resources.AbstractResourceStatusTable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Scope;
import org.vaadin.spring.VaadinComponent;

import javax.annotation.PostConstruct;
import java.util.Collection;
import java.util.stream.Collectors;

/**
 * Panel used in the {@link AssignmentView} to assign resources to the
 * assignment.
 */
@VaadinComponent
@Scope("prototype")
class AssignmentResourcesPanel extends HorizontalSplitPanel {

    @Autowired
    AssignableResourcesContainer assignableResourcesContainer;
    @Autowired
    AssignedResourcesContainer assignedResourcesContainer;
    @Autowired
    ResourceStatusService resourceStatusService;
    @Autowired
    ApplicationContext applicationContext;

    private AssignmentModel assignmentModel;
    private AbstractResourceStatusTable assignableResourcesTable;
    private AbstractResourceStatusTable assignedResourcesTable;
    private Button add;
    private Button remove;

    @PostConstruct
    void init() {
        setSplitPosition(200, Unit.PIXELS);
        setSizeFull();
        addStyleName("assignment-resources-panel");

        assignableResourcesTable = new AbstractResourceStatusTable(assignableResourcesContainer, applicationContext) {
            {
                setSizeFull();
                setSelectable(true);
                setMultiSelect(true);
                setVisibleColumns(ResourceStatus.PROP_RESOURCE, AssignableResourcesContainer.NESTPROP_RESOURCE_TYPE);
                setSortEnabled(true);
                setSortContainerPropertyId(ResourceStatus.PROP_RESOURCE);
            }
        };
        setFirstComponent(assignableResourcesTable);

        setSecondComponent(new HorizontalLayout() {
            {
                setSpacing(true);
                setSizeFull();

                addComponent(new VerticalLayout() {
                    {
                        setSpacing(true);
                        setSizeUndefined();

                        add = new Button("»", AssignmentResourcesPanel.this::assignSelectedResources);
                        add.setDescription("Add selected resources to assignment");
                        add.setDisableOnClick(true);
                        addComponent(add);

                        remove = new Button("«", AssignmentResourcesPanel.this::freeSelectedResources);
                        remove.setDescription("Remove selected resources from assignment");
                        remove.setDisableOnClick(true);
                        addComponent(remove);
                    }
                });

                assignedResourcesTable = new AbstractResourceStatusTable(assignedResourcesContainer, applicationContext) {
                    {
                        setSizeFull();
                        setSelectable(true);
                        setMultiSelect(true);
                        setVisibleColumns(ResourceStatus.PROP_RESOURCE, AssignedResourcesContainer.NESTPROP_RESOURCE_TYPE,
                                ResourceStatus.PROP_STATE, ResourceStatus.PROP_TIMESTAMP);
                        setSortEnabled(true);
                        setSortContainerPropertyId(ResourceStatus.PROP_RESOURCE);
                    }
                };
                addComponent(assignedResourcesTable);
                setExpandRatio(assignedResourcesTable, 1f);
            }
        });
    }

    @Override
    public void attach() {
        super.attach();
        assignableResourcesContainer.attachedToUI(getUI());
        assignedResourcesContainer.attachedToUI(getUI());
        assignableResourcesContainer.refresh();
        assignedResourcesContainer.refresh();
    }

    @Override
    public void detach() {
        assignableResourcesContainer.detachedFromUI(getUI());
        assignedResourcesContainer.detachedFromUI(getUI());
        super.detach();
    }

    void setAssignmentModel(AssignmentModel assignmentModel) {
        this.assignmentModel = assignmentModel;
        assignedResourcesContainer.setAssignmentModel(assignmentModel);
    }

    Collection<Resource> getSelectedAssignedResources() {
        return assignedResourcesTable.getCurrentSelection().stream().map(ResourceStatus::getResource).collect(Collectors.toSet());
    }

    private void assignSelectedResources(Button.ClickEvent clickEvent) {
        try {
            final Assignment assignment = assignmentModel.assignment().getValue();
            assignableResourcesTable.getCurrentSelection().forEach(
                    status -> resourceStatusService.setResourceAssignment(status.getResource(), assignment)
            );
        } finally {
            add.setEnabled(true);
        }
    }

    private void freeSelectedResources(Button.ClickEvent clickEvent) {
        try {
            assignedResourcesTable.getCurrentSelection().forEach(
                    status -> resourceStatusService.clearResourceAssignment(status.getResource())
            );
        } finally {
            remove.setEnabled(true);
        }
    }
}
