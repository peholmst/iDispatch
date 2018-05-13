package net.pkhapps.idispatch.web.ui.dispatch.model;

import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.ViewScope;
import net.pkhapps.idispatch.application.assignment.AssignmentDetailsDTO;
import net.pkhapps.idispatch.application.assignment.AssignmentService;
import net.pkhapps.idispatch.domain.assignment.AssignmentId;
import net.pkhapps.idispatch.domain.assignment.AssignmentPriority;
import net.pkhapps.idispatch.domain.assignment.AssignmentState;
import net.pkhapps.idispatch.domain.assignment.AssignmentTypeId;
import net.pkhapps.idispatch.domain.common.MunicipalityId;
import net.pkhapps.idispatch.web.ui.common.AbstractModel;
import net.pkhapps.idispatch.web.ui.common.model.*;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

import java.io.Serializable;
import java.time.Instant;

/**
 * TODO Document me!
 */
@SpringComponent
@ViewScope
public class AssignmentModel extends AbstractModel<AssignmentModel.Observer> {

    private final AssignmentService assignmentService;

    private final SimpleProperty<String> idAndVersion = new SimpleProperty<>(String.class);
    private final SimpleProperty<Instant> opened = new SimpleProperty<>(Instant.class);
    private final SimpleProperty<Instant> closed = new SimpleProperty<>(Instant.class);
    private final SimpleProperty<AssignmentState> state = new SimpleProperty<>(AssignmentState.class);
    private final SimpleWritableProperty<String> description = new SimpleWritableProperty<>(String.class);
    private final SimpleWritableProperty<AssignmentTypeId> type = new SimpleWritableProperty<>(AssignmentTypeId.class);
    private final SimpleWritableProperty<AssignmentPriority> priority = new SimpleWritableProperty<>(AssignmentPriority.class);
    private final SimpleWritableProperty<MunicipalityId> municipality = new SimpleWritableProperty<>(MunicipalityId.class);
    private final SimpleWritableProperty<String> address = new SimpleWritableProperty<>(String.class);
    private final AbstractAction<Void> close = new AbstractAction<>() {
        @Override
        protected Void doPerform() {
            // TODO implement me
            return null;
        }
    };

    private AssignmentDetailsDTO assignmentDetails;

    AssignmentModel(AssignmentService assignmentService) {
        this.assignmentService = assignmentService;
    }

    @NonNull
    public Property<String> idAndVersion() {
        return idAndVersion;
    }

    @NonNull
    public Property<Instant> opened() {
        return opened;
    }

    @NonNull
    public Property<Instant> closed() {
        return closed;
    }

    @NonNull
    public Property<AssignmentState> state() {
        return state;
    }

    @NonNull
    public WritableProperty<String> description() {
        return description;
    }

    @NonNull
    public WritableProperty<AssignmentTypeId> type() {
        return type;
    }

    @NonNull
    public WritableProperty<AssignmentPriority> priority() {
        return priority;
    }

    @NonNull
    public WritableProperty<MunicipalityId> municipality() {
        return municipality;
    }

    @NonNull
    public WritableProperty<String> address() {
        return address;
    }

    @NonNull
    public Action<Void> close() {
        return close;
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
