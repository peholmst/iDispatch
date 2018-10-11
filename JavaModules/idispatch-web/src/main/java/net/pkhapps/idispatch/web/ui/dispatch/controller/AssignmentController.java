package net.pkhapps.idispatch.web.ui.dispatch.controller;

import com.vaadin.navigator.Navigator;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.UIScope;
import net.pkhapps.idispatch.application.assignment.AssignmentService;
import net.pkhapps.idispatch.web.ui.dispatch.navigation.AssignmentDetailsViewNavigationRequest;

/**
 * TODO Document me!
 */
@SpringComponent
@UIScope
public class AssignmentController {

    private final Navigator navigator;
    private final AssignmentService assignmentService;

    AssignmentController(Navigator navigator,
                         AssignmentService assignmentService) {
        this.navigator = navigator;
        this.assignmentService = assignmentService;
    }

    public void openAssignment() {
        new AssignmentDetailsViewNavigationRequest(assignmentService.openAssignment()).navigate(navigator);
    }
}
