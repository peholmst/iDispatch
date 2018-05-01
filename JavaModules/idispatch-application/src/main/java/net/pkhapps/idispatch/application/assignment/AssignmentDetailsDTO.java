package net.pkhapps.idispatch.application.assignment;

import net.pkhapps.idispatch.domain.assignment.AssignmentId;
import net.pkhapps.idispatch.domain.assignment.AssignmentPriority;
import net.pkhapps.idispatch.domain.assignment.AssignmentState;
import net.pkhapps.idispatch.domain.assignment.AssignmentTypeId;
import net.pkhapps.idispatch.domain.common.MunicipalityId;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * TODO Document me!
 */
public class AssignmentDetailsDTO implements Serializable, Cloneable {

    private AssignmentId id;
    private Long version;
    private Instant opened;
    private Instant closed;
    private AssignmentState state;
    private String description;
    private AssignmentTypeId type;
    private AssignmentPriority priority;
    private MunicipalityId municipality;
    private String address;

    public AssignmentDetailsDTO(@NonNull AssignmentId id, @NonNull Long version, @NonNull Instant opened,
                                @Nullable Instant closed, @NonNull AssignmentState state) {
        this.id = Objects.requireNonNull(id);
        this.version = Objects.requireNonNull(version);
        this.opened = Objects.requireNonNull(opened);
        this.closed = closed;
        this.state = Objects.requireNonNull(state);
    }

    @NonNull
    public AssignmentId getId() {
        return id;
    }

    @NonNull
    public Long getVersion() {
        return version;
    }

    @NonNull
    public Instant getOpened() {
        return opened;
    }

    @Nullable
    public Instant getClosed() {
        return closed;
    }

    @NonNull
    public AssignmentState getState() {
        return state;
    }

    @Nullable
    public String getDescription() {
        return description;
    }

    public void setDescription(@Nullable String description) {
        this.description = description;
    }

    @Nullable
    public AssignmentTypeId getType() {
        return type;
    }

    public void setType(@Nullable AssignmentTypeId type) {
        this.type = type;
    }

    @Nullable
    public AssignmentPriority getPriority() {
        return priority;
    }

    public void setPriority(@Nullable AssignmentPriority priority) {
        this.priority = priority;
    }

    @Nullable
    public MunicipalityId getMunicipality() {
        return municipality;
    }

    public void setMunicipality(@Nullable MunicipalityId municipality) {
        this.municipality = municipality;
    }

    @Nullable
    public String getAddress() {
        return address;
    }

    public void setAddress(@Nullable String address) {
        this.address = address;
    }

    @NonNull
    public String getIdAndVersion() {
        return String.format("%d (%d)", id.toLong(), version);
    }

    @Override
    public AssignmentDetailsDTO clone() {
        try {
            return (AssignmentDetailsDTO) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
    }
}
