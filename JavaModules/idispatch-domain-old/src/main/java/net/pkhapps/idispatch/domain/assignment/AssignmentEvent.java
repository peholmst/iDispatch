package net.pkhapps.idispatch.domain.assignment;

import net.pkhapps.idispatch.domain.assignment.Assignment;
import org.springframework.lang.NonNull;

import java.util.Objects;

/**
 * Base class for domain events that involve an {@link Assignment}.
 */
public abstract class AssignmentEvent {

    private final Assignment assignment;

    public AssignmentEvent(@NonNull Assignment assignment) {
        this.assignment = Objects.requireNonNull(assignment);
    }

    @NonNull
    public Assignment getAssignment() {
        return assignment;
    }
}
