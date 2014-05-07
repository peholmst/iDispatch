package net.pkhsolutions.idispatch.boundary.events;

import net.pkhsolutions.idispatch.entity.Assignment;
import org.springframework.context.ApplicationEvent;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Base class for assignment events.
 */
public abstract class AssignmentEvent extends ApplicationEvent {

    private final Assignment assignment;

    protected AssignmentEvent(Object source, Assignment assignment) {
        super(source);
        this.assignment = checkNotNull(assignment);
    }

    public Assignment getAssignment() {
        return assignment;
    }
}
