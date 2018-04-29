package net.pkhapps.idispatch.web.ui.dispatch.provider;

import com.vaadin.data.ValueProvider;
import net.pkhapps.idispatch.domain.assignment.AssignmentState;
import net.pkhapps.idispatch.domain.status.ResourceState;

/**
 * TODO Implement me!
 */
public class AssignmentStateStyleProvider implements ValueProvider<AssignmentState, String> {

    @Override
    public String apply(AssignmentState assignmentState) {
        if (assignmentState == null) {
            return null;
        } else {
            return String.format("assignment-state-%s", assignmentState);
        }
    }
}
