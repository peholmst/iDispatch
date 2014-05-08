package net.pkhsolutions.idispatch.boundary;

import net.pkhsolutions.idispatch.entity.Assignment;
import net.pkhsolutions.idispatch.utils.UpdateResult;

import java.util.List;
import java.util.Optional;

/**
 * Service interface for assignment operations.
 */
public interface AssignmentService {

    /**
     * Finds the assignment with the specified ID. If the ID is {@code null}, or the assignment
     * does not exist, an empty {@code Optional} is returned.
     */
    Optional<Assignment> findAssignment(Long id);

    /**
     * Creates a new assignment, stores it, publishes an {@link net.pkhsolutions.idispatch.boundary.events.AssignmentOpened} event
     * and returns the assignment ID.
     */
    Long openAssignment();

    /**
     * Saves the existing assignment, publishes an {@link net.pkhsolutions.idispatch.boundary.events.AssignmentUpdated} event
     * and returns:
     * <ul>
     * <li>{@link net.pkhsolutions.idispatch.utils.UpdateResult.Success} if the operation was successful.</li>
     * <li>{@link net.pkhsolutions.idispatch.utils.UpdateResult.Conflict} if the assignment had been modified by another user.</li>
     * <li>{@link net.pkhsolutions.idispatch.utils.UpdateResult.NoChange} if the assignment did not exist or was closed.</li>
     * </ui>
     */
    UpdateResult<Assignment> updateAssignment(Assignment assignment);

    /**
     * Closes the assignment and fires an {@link net.pkhsolutions.idispatch.boundary.events.AssignmentClosed} event. If
     * the assignment is already closed, or there are still resources assigned to the assignment, nothing will happen.
     */
    void closeAssignment(Assignment assignment);

    /**
     * Finds all currently open assignments.
     */
    List<Assignment> findOpenAssignments();
}
