package net.pkhsolutions.idispatch.boundary.events;

import net.pkhsolutions.idispatch.entity.Assignment;

/**
 * Event published when a new assignment is opened.
 */
public class AssignmentOpened extends AssignmentEvent {
    public AssignmentOpened(Object source, Assignment assignment) {
        super(source, assignment);
    }
}
