package net.pkhsolutions.idispatch.ui.dws.assignments;

import com.vaadin.ui.MenuBar;
import net.pkhsolutions.idispatch.boundary.AssignmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.vaadin.spring.VaadinComponent;

/**
 * Menu bar command that creates and opens a new assignment.
 */
@VaadinComponent
@Scope("prototype")
public class OpenAssignmentCommand implements MenuBar.Command {

    @Autowired
    AssignmentService assignmentService;

    @Override
    public void menuSelected(MenuBar.MenuItem selectedItem) {
        final Long newAssignmentId = assignmentService.openAssignment();
        AssignmentView.openAssignment(newAssignmentId);
    }
}
