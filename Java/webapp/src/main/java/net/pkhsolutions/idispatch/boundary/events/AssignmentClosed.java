package net.pkhsolutions.idispatch.boundary.events;

import net.pkhsolutions.idispatch.entity.Assignment;

/**
 * Event published when an open assignment is closed.
 */
public class AssignmentClosed extends AssignmentEvent {

    public AssignmentClosed(Object source, Assignment assignment) {
        super(source, assignment);
    }
}
