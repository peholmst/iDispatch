package net.pkhapps.idispatch.web.ui.dispatch.lookup;

import com.vaadin.ui.ComboBox;
import net.pkhapps.idispatch.domain.assignment.AssignmentPriority;

/**
 * Combo box for looking up/selecting assignment priorities.
 */
public class AssignmentPriorityComboBox extends ComboBox<AssignmentPriority> {

    public AssignmentPriorityComboBox() {
        setItems(AssignmentPriority.values());
    }
}
