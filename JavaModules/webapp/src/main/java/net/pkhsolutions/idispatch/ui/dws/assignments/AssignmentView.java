package net.pkhsolutions.idispatch.ui.dws.assignments;

import com.vaadin.data.Property;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.ui.*;
import net.pkhsolutions.idispatch.boundary.AssignmentService;
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
    AssignmentResourcesPanel assignmentResourcesPanel;

    private Button close;

    // TODO Internationalize
    // TODO Map

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
    }

    private Component createAssignmentContent() {
        return new VerticalLayout() {
            {
                setSizeFull();
                setMargin(true);
                setSpacing(true);

                assignmentForm.setWidth(100, Unit.PERCENTAGE);
                assignmentForm.setTicketModel(assignmentModel);
                addComponent(assignmentForm);

                addComponent(close = new Button("Close Assignment",
                        AssignmentView.this::closeAssignment));
                close.setDisableOnClick(true);

                final Label resourcesTitle = new Label("Resources to dispatch");
                resourcesTitle.addStyleName(DwsTheme.LABEL_H2);
                addComponent(resourcesTitle);

                assignmentResourcesPanel.setAssignmentModel(assignmentModel);
                addComponent(assignmentResourcesPanel);
                setExpandRatio(assignmentResourcesPanel, 1f);
            }
        };
    }

    private void closeAssignment(Button.ClickEvent clickEvent) {
        try {
            if (!assignmentService.closeAssignment(assignmentModel.assignment().getValue())) {
                Notification.show("Could not close assignment", "There are still resources attached to this assignment", Notification.Type.HUMANIZED_MESSAGE);
            }
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
            ((Property.ValueChangeNotifier) assignmentModel.isClosed()).addValueChangeListener(event -> updateCloseButtonState());
            setContent(createAssignmentContent());
            updateCloseButtonState();
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

    private void updateCloseButtonState() {
        close.setVisible(!assignmentModel.isClosed().getValue());
    }
}
