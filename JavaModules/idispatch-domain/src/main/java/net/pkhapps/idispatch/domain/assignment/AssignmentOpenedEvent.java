package net.pkhapps.idispatch.domain.assignment;

/**
 * Domain event published when an assignment is opened.
 */
public class AssignmentOpenedEvent extends AssignmentEvent {

    public AssignmentOpenedEvent(Assignment assignment) {
        super(assignment);
    }
}
