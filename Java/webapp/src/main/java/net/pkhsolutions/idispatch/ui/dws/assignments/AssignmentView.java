package net.pkhsolutions.idispatch.ui.dws.assignments;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.ui.*;
import net.pkhsolutions.idispatch.boundary.AssignmentService;
import net.pkhsolutions.idispatch.boundary.DispatchService;
import net.pkhsolutions.idispatch.entity.Assignment;
import net.pkhsolutions.idispatch.ui.dws.DwsTheme;
import net.pkhsolutions.idispatch.ui.dws.DwsUI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.vaadin.spring.navigator.VaadinView;

import javax.annotation.PostConstruct;
import java.util.Optional;

/**
 * View for editing a single assignment.
 */
@VaadinView(name = AssignmentView.VIEW_NAME, ui = DwsUI.class)
@Scope("prototype")
public class AssignmentView extends Panel implements View {

    public static final String VIEW_NAME = "assignment";

    @Autowired
    AssignmentModel assignmentModel;
    @Autowired
    AssignmentForm assignmentForm;
    @Autowired
    AssignmentService assignmentService;
    @Autowired
    DispatchService dispatchService;
    @Autowired
    AssignmentResourcesPanel assignmentResourcesPanel;
    @Autowired
    AssignmentMapPanel assignmentMapPanel;

    private Button dispatchAll;
    private Button dispatchSelected;
    private Button dispatchReserved;
    private Button close;

    // TODO Internationalize

    /**
     * Navigates to the assignment with the specified ID in the current UI.
     */
    public static void openAssignment(Long assignmentId) {
        UI.getCurrent().getNavigator().navigateTo(VIEW_NAME + "/" + assignmentId);
    }

    @PostConstruct
    void init() {
        setSizeFull();
        addStyleName(DwsTheme.PANEL_LIGHT);
        // TODO Disable components when the ticket is closed
    }

    private Component createAssignmentContent() {
        return new HorizontalSplitPanel() {{
            addStyleName(DwsTheme.SPLITPANEL_SMALL);
            setSizeFull();
            setFirstComponent(new VerticalLayout() {
                {
                    setSizeFull();
                    setMargin(true);
                    setSpacing(true);

                    addComponent(new HorizontalLayout() {
                        {
                            setSpacing(true);
                            setWidth(100, Unit.PERCENTAGE);

                            assignmentForm.setWidth(100, Unit.PERCENTAGE);
                            assignmentForm.setTicketModel(assignmentModel);
                            addComponent(assignmentForm);
                            setExpandRatio(assignmentForm, 1f);

                            addComponent(new VerticalLayout() {
                                {
                                    setSpacing(true);
                                    setSizeUndefined();

                                    addComponent(dispatchAll = new Button("Dispatch All Resources",
                                            AssignmentView.this::dispatchAllResources));
                                    dispatchAll.setDisableOnClick(true);

                                    addComponent(dispatchSelected = new Button("Dispatch Selected Resources",
                                            AssignmentView.this::dispatchSelectedResources));
                                    dispatchSelected.setDisableOnClick(true);

                                    addComponent(dispatchReserved = new Button("Dispatch Reserved Resources",
                                            AssignmentView.this::dispatchReservedResources));
                                    dispatchReserved.setDisableOnClick(true);

                                    addComponent(close = new Button("Close Assignment",
                                            AssignmentView.this::closeAssignment));
                                    close.setDisableOnClick(true);
                                }
                            });
                        }
                    });

                    final Label resourcesTitle = new Label("Resources to dispatch");
                    resourcesTitle.addStyleName(DwsTheme.LABEL_H2);
                    addComponent(resourcesTitle);

                    assignmentResourcesPanel.setAssignmentModel(assignmentModel);
                    addComponent(assignmentResourcesPanel);
                    setExpandRatio(assignmentResourcesPanel, 1f);
                }
            });
            assignmentMapPanel.setAssignmentModel(assignmentModel);
            setSecondComponent(assignmentMapPanel);
        }};
    }

    private void dispatchAllResources(Button.ClickEvent clickEvent) {
        try {
            dispatchService.dispatchAllResources(assignmentModel.assignment().getValue());
        } finally {
            dispatchAll.setEnabled(true);
        }
    }

    private void dispatchSelectedResources(Button.ClickEvent clickEvent) {
        try {
            dispatchService.dispatchSelectedResources(assignmentModel.assignment().getValue(), assignmentResourcesPanel.getSelectedAssignedResources());
        } finally {
            dispatchSelected.setEnabled(true);
        }
    }

    private void dispatchReservedResources(Button.ClickEvent clickEvent) {
        try {
            dispatchService.dispatchAllReservedResources(assignmentModel.assignment().getValue());
        } finally {
            dispatchReserved.setEnabled(true);
        }
    }

    private void closeAssignment(Button.ClickEvent clickEvent) {
        try {
            assignmentService.closeAssignment(assignmentModel.assignment().getValue());
        } finally {
            close.setEnabled(true);
        }
    }

    private Component createNotFoundContent() {
        return new Label("You attempted to open an assignment that does not exist.");
    }

    @Override
    public void attach() {
        super.attach();
        assignmentModel.attachedToUI(getUI());
    }

    @Override
    public void detach() {
        assignmentModel.detachedFromUI(getUI());
        super.detach();
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent viewChangeEvent) {
        final Long assignmentId = getAssignmentId(viewChangeEvent.getParameters());
        final Optional<Assignment> assignment = assignmentService.findAssignment(assignmentId);
        if (assignment.isPresent()) {
            assignmentModel.assignment().setValue(assignment.get());
            setContent(createAssignmentContent());
        } else {
            setContent(createNotFoundContent());
        }
    }

    private Long getAssignmentId(String viewParameters) {
        try {
            return Long.valueOf(viewParameters);
        } catch (NumberFormatException ex) {
            return null;
        }
    }
}
