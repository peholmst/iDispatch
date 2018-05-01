package net.pkhapps.idispatch.web.ui.dispatch.model;

import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.ViewScope;
import net.pkhapps.idispatch.application.assignment.AssignmentDetailsDTO;
import net.pkhapps.idispatch.application.assignment.AssignmentService;
import net.pkhapps.idispatch.domain.assignment.AssignmentId;
import net.pkhapps.idispatch.web.ui.common.AbstractModel;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

import java.io.Serializable;

/**
 * TODO Document me!
 */
@SpringComponent
@ViewScope
public class AssignmentModel extends AbstractModel<AssignmentModel.Observer> {

    private final AssignmentService assignmentService;

    private AssignmentDetailsDTO assignmentDetails;

    AssignmentModel(AssignmentService assignmentService) {
        this.assignmentService = assignmentService;
    }

    /**
     * Returns the details of the current assignment or {@code null} if the model is empty. The returned DTO is a clone
     * so changing it will not change the state of this model. Clients should call
     * {@link #updateDetails(AssignmentDetailsDTO)} to update the model state.
     */
    @Nullable
    public AssignmentDetailsDTO getDetails() {
        return assignmentDetails == null ? null : assignmentDetails.clone();
    }

    /**
     * @param updatedDetails
     * @throws OptimisticLockingFailureException
     */
    public void updateDetails(@NonNull AssignmentDetailsDTO updatedDetails) throws OptimisticLockingFailureException {

    }

    /**
     * Loads the assignment with the given ID into the model. If no such assignment exists, nothing happens. Clients can
     * check {@link #isEmpty()} or {@link #getDetails()} to see if the assignment was loaded successfully or not.
     */
    public void loadAssignmentIntoModel(@NonNull AssignmentId assignmentId) {
        assignmentService.findAssignmentDetails(assignmentId).ifPresent(this::setAssignmentDetails);
    }

    private void setAssignmentDetails(@Nullable AssignmentDetailsDTO assignmentDetails) {
        this.assignmentDetails = assignmentDetails;
        notifyObservers(observer -> observer.onAssignmentDetailsChanged(this));
    }

    /**
     * Returns whether the model is empty or contains an assignment.
     */
    public boolean isEmpty() {
        return assignmentDetails == null;
    }

    public interface Observer extends Serializable {

        default void onAssignmentDetailsChanged(@NonNull AssignmentModel model) {
            // NOP
        }
    }
}
