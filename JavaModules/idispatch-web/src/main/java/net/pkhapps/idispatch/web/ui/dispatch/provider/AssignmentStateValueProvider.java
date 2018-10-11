package net.pkhapps.idispatch.web.ui.dispatch.provider;

import com.vaadin.data.ValueProvider;
import net.pkhapps.idispatch.domain.assignment.AssignmentState;
import net.pkhapps.idispatch.web.ui.common.i18n.I18N;

/**
 * TODO implement me
 */
public class AssignmentStateValueProvider implements ValueProvider<AssignmentState, String> {

    private final I18N i18n;

    public AssignmentStateValueProvider(I18N i18n) {
        this.i18n = i18n;
    }

    @Override
    public String apply(AssignmentState assignmentState) {
        if (assignmentState == null) {
            return "";
        } else {
            return i18n.get(String.format("assignmentState.%s", assignmentState));
        }
    }
}
