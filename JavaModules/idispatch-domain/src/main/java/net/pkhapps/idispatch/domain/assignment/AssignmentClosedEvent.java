package net.pkhapps.idispatch.domain.assignment;

/**
 * Domain event published when an assignment is closed.
 */
public class AssignmentClosedEvent extends AssignmentEvent {

    public AssignmentClosedEvent(Assignment assignment) {
        super(assignment);
    }
}
