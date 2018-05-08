package net.pkhapps.idispatch.domain.assignment;

import net.pkhapps.idispatch.domain.base.AbstractEntity;
import net.pkhapps.idispatch.domain.resource.ResourceId;
import net.pkhapps.idispatch.domain.status.ResourceState;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

import javax.persistence.*;
import java.time.Instant;
import java.util.Objects;

@Entity
@Table(name = "assignment_resources")
public class AssignmentResource extends AbstractEntity {

    @ManyToOne(optional = false)
    @JoinColumn(name = "assignment_id", nullable = false)
    private Assignment assignment;

    @Column(name = "resource_id", nullable = false)
    private ResourceId resource;

    @Column(name = "reserved_ts")
    private Instant reserved;

    @Column(name = "dispatched_ts")
    private Instant dispatched;

    @Column(name = "en_route_ts")
    private Instant enRoute;

    @Column(name = "on_scene_ts")
    private Instant onScene;

    @Column(name = "available_ts")
    private Instant available;

    @Column(name = "at_station_ts")
    private Instant atStation;

    @SuppressWarnings("unused")
    private AssignmentResource() {
        // Used by JPA only.
    }

    public AssignmentResource(@NonNull Assignment assignment,
                              @NonNull ResourceId resource) {
        this.assignment = Objects.requireNonNull(assignment);
        this.resource = Objects.requireNonNull(resource);
    }

    @NonNull
    public Assignment getAssignment() {
        return assignment;
    }

    @NonNull
    public ResourceId getResource() {
        return resource;
    }

    @Nullable
    public Instant getReserved() {
        return reserved;
    }

    @Nullable
    public Instant getDispatched() {
        return dispatched;
    }

    @Nullable
    public Instant getEnRoute() {
        return enRoute;
    }

    @Nullable
    public Instant getOnScene() {
        return onScene;
    }

    @Nullable
    public Instant getAvailable() {
        return available;
    }

    @Nullable
    public Instant getAtStation() {
        return atStation;
    }

    boolean isAssigned() {
        return available == null && atStation == null;
    }

    boolean canSetTimestampFor(@NonNull ResourceState state) {
        var result = true;
        switch (state) {
            case RESERVED:
                result = reserved == null;
            case DISPATCHED:
                result = result && dispatched == null;
            case EN_ROUTE:
                result = result && enRoute == null;
            case ON_SCENE:
                result = result && onScene == null;
            case AVAILABLE:
                result = result && available == null;
            case AT_STATION:
                result = result && atStation == null;
                break;
            default:
                result = false;
        }
        return result;
    }

    void setTimestampFor(@NonNull ResourceState state, @NonNull Instant stateChangedOn) {
        assignment.requireOpen();
        if (!canSetTimestampFor(state)) {
            throw new IllegalStateException("The timestamp for " + state + " cannot be set");
        }
        switch (state) {
            case RESERVED:
                reserved = stateChangedOn;
                return;
            case DISPATCHED:
                dispatched = stateChangedOn;
                return;
            case EN_ROUTE:
                enRoute = stateChangedOn;
                return;
            case ON_SCENE:
                onScene = stateChangedOn;
                return;
            case AVAILABLE:
                available = stateChangedOn;
                return;
            case AT_STATION:
                atStation = stateChangedOn;
        }
    }
}
