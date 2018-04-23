package net.pkhsolutions.idispatch.ui.dws.assignments;

import com.vaadin.data.Property;
import com.vaadin.ui.*;
import net.pkhsolutions.idispatch.boundary.DispatchService;
import net.pkhsolutions.idispatch.boundary.ResourceStatusService;
import net.pkhsolutions.idispatch.entity.Assignment;
import net.pkhsolutions.idispatch.entity.Resource;
import net.pkhsolutions.idispatch.entity.ResourceStatus;
import net.pkhsolutions.idispatch.entity.ValidationFailedException;
import net.pkhsolutions.idispatch.ui.common.ValidationFailedExceptionHandler;
import net.pkhsolutions.idispatch.ui.dws.resources.AbstractResourceStatusTable;
import net.pkhsolutions.idispatch.ui.dws.resources.ChangeResourceStatusWindow;
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
    DispatchService dispatchService;
    @Autowired
    ApplicationContext applicationContext;
    @Autowired
    ChangeResourceStatusWindow changeResourceStatusWindow;

    private AssignmentModel assignmentModel;
    private AbstractResourceStatusTable assignableResourcesTable;
    private AbstractResourceStatusTable assignedResourcesTable;
    private Button add;
    private Button remove;
    private Button dispatchAll;
    private Button dispatchSelected;
    private Button dispatchReserved;
    private Button changeState;

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
                        setImmediate(true);
                        addValueChangeListener(event -> updateComponentStates());
                    }
                };
                addComponent(assignedResourcesTable);
                setExpandRatio(assignedResourcesTable, 1f);

                addComponent(new VerticalLayout() {
                    {
                        setWidth("150px");
                        setHeight("100%");
                        setSpacing(true);

                        addComponent(dispatchAll = new NativeButton("Dispatch All",
                                AssignmentResourcesPanel.this::dispatchAllResources));
                        dispatchAll.addStyleName("dispatch");
                        dispatchAll.setDisableOnClick(true);
                        dispatchAll.setWidth("100%");

                        addComponent(dispatchSelected = new NativeButton("Dispatch Selected",
                                AssignmentResourcesPanel.this::dispatchSelectedResources));
                        dispatchSelected.addStyleName("dispatch");
                        dispatchSelected.setDisableOnClick(true);
                        dispatchSelected.setWidth("100%");

                        addComponent(dispatchReserved = new NativeButton("Dispatch Reserved",
                                AssignmentResourcesPanel.this::dispatchReservedResources));
                        dispatchReserved.addStyleName("dispatch");
                        dispatchReserved.setDisableOnClick(true);
                        dispatchReserved.setWidth("100%");

                        addComponent(changeState = new Button("Change State",
                                AssignmentResourcesPanel.this::changeState));
                        changeState.setDisableOnClick(true);
                        changeState.setWidth("100%");
                        setExpandRatio(changeState, 1f);
                        setComponentAlignment(changeState, Alignment.BOTTOM_LEFT);
                    }
                });
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
        ((Property.ValueChangeNotifier) assignmentModel.isClosed()).addValueChangeListener(event -> {
            updateComponentStates();
        });
        updateComponentStates();
    }

    private Collection<Resource> getSelectedAssignedResources() {
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

    private void dispatchAllResources(Button.ClickEvent clickEvent) {
        try {
            dispatchService.dispatchAllResources(assignmentModel.assignment().getValue());
        } catch (ValidationFailedException ex) {
            ValidationFailedExceptionHandler.showValidationErrors(getUI(), ex);
        } finally {
            dispatchAll.setEnabled(true);
        }
    }

    private void dispatchSelectedResources(Button.ClickEvent clickEvent) {
        try {
            dispatchService.dispatchSelectedResources(assignmentModel.assignment().getValue(), getSelectedAssignedResources());
        } catch (ValidationFailedException ex) {
            ValidationFailedExceptionHandler.showValidationErrors(getUI(), ex);
        } finally {
            dispatchSelected.setEnabled(true);
        }
    }

    private void dispatchReservedResources(Button.ClickEvent clickEvent) {
        try {
            dispatchService.dispatchAllReservedResources(assignmentModel.assignment().getValue());
        } catch (ValidationFailedException ex) {
            ValidationFailedExceptionHandler.showValidationErrors(getUI(), ex);
        } finally {
            dispatchReserved.setEnabled(true);
        }
    }

    private void changeState(Button.ClickEvent clickEvent) {
        try {
            Collection<Resource> selection = getSelectedAssignedResources();
            if (selection.size() == 1) {
                changeResourceStatusWindow.open(getUI(), selection.iterator().next());
            }
        } finally {
            changeState.setEnabled(true);
        }
    }

    private void updateComponentStates() {
        if (assignmentModel.isClosed().getValue()) {
            setEnabled(false);
        } else {
            final Collection<Resource> selectedAssignedResources = getSelectedAssignedResources();
            changeState.setEnabled(selectedAssignedResources.size() == 1);
            dispatchSelected.setEnabled(selectedAssignedResources.size() > 0);
        }
    }
}
