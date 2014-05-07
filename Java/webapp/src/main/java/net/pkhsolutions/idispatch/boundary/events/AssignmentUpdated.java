package net.pkhsolutions.idispatch.boundary.events;

import net.pkhsolutions.idispatch.entity.Assignment;

/**
 * Event published when an open assignment is updated (but not closed).
 */
public class AssignmentUpdated extends AssignmentEvent {
    public AssignmentUpdated(Object source, Assignment assignment) {
        super(source, assignment);
    }
}
