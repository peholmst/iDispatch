package net.pkhapps.idispatch.web.ui.dispatch.navigation;

import com.vaadin.navigator.ViewChangeListener;
import net.pkhapps.idispatch.domain.assignment.AssignmentId;
import net.pkhapps.idispatch.web.ui.common.AbstractNavigationRequest;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

/**
 * TODO Document me!
 */
public class AssignmentDetailsViewNavigationRequest extends AbstractNavigationRequest {

    public static final String VIEW_NAME = "assignmentDetails";
    private static final String ID_PARAMETER = "id";

    private AssignmentDetailsViewNavigationRequest() {
        registerParameter(ID_PARAMETER, AssignmentId.class, AssignmentId::toString, AssignmentId::new);
    }

    public AssignmentDetailsViewNavigationRequest(@NonNull ViewChangeListener.ViewChangeEvent viewChangeEvent) {
        this();
        readParametersFromEvent(viewChangeEvent);
    }

    public AssignmentDetailsViewNavigationRequest(@NonNull AssignmentId assignmentId) {
        this();
        setParameter(ID_PARAMETER, assignmentId);
    }

    @Nullable
    public AssignmentId getAssignmentId() {
        return getParameter(ID_PARAMETER, AssignmentId.class);
    }

    @Override
    protected String getViewName() {
        return VIEW_NAME;
    }
}
