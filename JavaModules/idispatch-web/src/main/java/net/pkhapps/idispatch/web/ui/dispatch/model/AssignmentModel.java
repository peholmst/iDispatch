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
import net.pkhapps.idispatch.web.ui.common.model.*;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

import java.io.Serializable;
import java.time.Instant;

/**
 * TODO Document me!
 */
@SpringComponent
@ViewScope
public class AssignmentModel implements Serializable {

    // TODO Update the assignment when the user changes any writable fields
    // TODO Register for domain events and update the model when the assignment is changed

    private final AssignmentService assignmentService;

    private final SimpleProperty<AssignmentId> id = new SimpleProperty<>(AssignmentId.class);
    private final SimpleProperty<String> idAndVersion = new SimpleProperty<>(String.class);
    private final SimpleProperty<Instant> opened = new SimpleProperty<>(Instant.class);
    private final SimpleProperty<Instant> closed = new SimpleProperty<>(Instant.class);
    private final SimpleProperty<AssignmentState> state = new SimpleProperty<>(AssignmentState.class);
    private final SimpleWritableProperty<String> description = new SimpleWritableProperty<>(String.class);
    private final SimpleWritableProperty<AssignmentTypeId> type = new SimpleWritableProperty<>(AssignmentTypeId.class);
    private final SimpleWritableProperty<AssignmentPriority> priority = new SimpleWritableProperty<>(AssignmentPriority.class);
    private final SimpleWritableProperty<MunicipalityId> municipality = new SimpleWritableProperty<>(MunicipalityId.class);
    private final SimpleWritableProperty<String> address = new SimpleWritableProperty<>(String.class);
    private AssignmentDetailsDTO assignmentDetails;
    private final VoidActionWrapper close = new VoidActionWrapper(this::closeCurrentAssignment);

    AssignmentModel(@NonNull AssignmentService assignmentService) {
        this.assignmentService = assignmentService;
        setAssignmentDetails(null); // Initialize the form
    }

    @NonNull
    public Property<AssignmentId> id() {
        return id;
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
     * Loads the assignment with the given ID into the model. If no such assignment exists, the model is reset. Clients can
     * check e.g. the {@link #id()} property to see if the assignment was loaded successfully or not.
     */
    public void loadAssignmentIntoModel(@NonNull AssignmentId assignmentId) {
        setAssignmentDetails(assignmentService.findAssignmentDetails(assignmentId).orElse(null));
    }

    private void closeCurrentAssignment() {
        assert assignmentDetails != null; // The action should make sure this is always true when this method is called.
        assignmentService.closeAssignment(assignmentDetails.getId());
    }

    private void setAssignmentDetails(@Nullable AssignmentDetailsDTO assignmentDetails) {
        this.assignmentDetails = assignmentDetails;
        if (assignmentDetails != null) {
            idAndVersion.setValue(assignmentDetails.getIdAndVersion());
            opened.setValue(assignmentDetails.getOpened());
            closed.setValue(assignmentDetails.getClosed());
            state.setValue(assignmentDetails.getState());
            description.setValue(assignmentDetails.getDescription());
            type.setValue(assignmentDetails.getType());
            priority.setValue(assignmentDetails.getPriority());
            municipality.setValue(assignmentDetails.getMunicipality());
            address.setValue(assignmentDetails.getAddress());
            close.setExecutable(assignmentDetails.getState() != AssignmentState.CLOSED);
        } else {
            idAndVersion.clear();
            opened.clear();
            closed.clear();
            state.clear();
            description.clear();
            type.clear();
            priority.clear();
            municipality.clear();
            address.clear();
            close.setExecutable(false);
        }
    }
}
