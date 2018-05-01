package net.pkhapps.idispatch.application.assignment;

import net.pkhapps.idispatch.domain.assignment.AssignmentId;
import org.springframework.lang.NonNull;

import java.util.Optional;

/**
 * TODO Document me
 */
public interface AssignmentService {

    /**
     * Closes the specified assignment. If the assignment has already been closed, nothing happens. If the assignment
     * is closed, a {@link net.pkhapps.idispatch.domain.assignment.AssignmentClosedEvent} is published.
     */
    void closeAssignment(@NonNull AssignmentId assignmentId);

    /**
     * Opens a new assignment, stores it in the database and returns the ID. An
     * {@link net.pkhapps.idispatch.domain.assignment.AssignmentOpenedEvent} is published.
     */
    @NonNull
    AssignmentId openAssignment();

    /**
     * Returns the details of the specified ID, regardless of its state.
     */
    @NonNull
    Optional<AssignmentDetailsDTO> findAssignmentDetails(@NonNull AssignmentId assignmentId);

    /**
     * Updates the details of the given assignment, provided that the assignment is open. If the assignment is closed,
     * nothing happens.
     *
     * @throws AssignmentUpdatedInOtherSessionException if another user has updated the details after the specified details
     *                                                  where retrieved from the service.
     */
    void updateDetailsOfOpenAssignment(@NonNull AssignmentDetailsDTO updatedDetails)
            throws AssignmentUpdatedInOtherSessionException;
}
