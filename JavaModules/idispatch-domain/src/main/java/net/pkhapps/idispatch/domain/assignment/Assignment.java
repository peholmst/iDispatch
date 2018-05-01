package net.pkhapps.idispatch.domain.assignment;

import net.pkhapps.idispatch.domain.base.AbstractAggregateRoot;
import net.pkhapps.idispatch.domain.common.MunicipalityId;
import net.pkhapps.idispatch.domain.resource.ResourceId;
import net.pkhapps.idispatch.domain.status.ResourceState;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

import javax.persistence.*;
import java.time.Clock;
import java.time.Instant;
import java.util.*;

import static net.pkhapps.idispatch.domain.assignment.AssignmentResourcePredicates.withResource;
import static net.pkhapps.idispatch.domain.assignment.AssignmentResourcePredicates.withSettableTimestampFor;

/**
 * Entity representing an assignment (e.g. an accident that rescue units need to respond to).
 */
@Entity
@Table(name = "assignments")
public class Assignment extends AbstractAggregateRoot<AssignmentId> {

    @Column(name = "opened", nullable = false)
    private Instant opened;

    @Column(name = "closed")
    private Instant closed;

    @Enumerated(EnumType.STRING)
    @Column(name = "state", nullable = false)
    private AssignmentState state = AssignmentState.NEW;

    @Enumerated(EnumType.STRING)
    @Column(name = "priority", nullable = false)
    private AssignmentPriority priority = AssignmentPriority.C;

    @Column(name = "type_id")
    private AssignmentTypeId type;

    @Column(name = "description", nullable = false)
    private String description = "";

    @Column(name = "municipality_id")
    private MunicipalityId municipality;

    @Column(name = "address", nullable = false)
    private String address = "";

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "assignment")
    private Set<AssignmentResource> assignmentResources = new HashSet<>();

    @SuppressWarnings("unused")
    private Assignment() {
        // Used by JPA only.
    }

    public Assignment(@NonNull Clock clock) {
        Objects.requireNonNull(clock);
        opened = clock.instant();
        registerEvent(new AssignmentOpenedEvent(this));
    }

    public void close(@NonNull Clock clock) {
        requireOpen();
        closed = clock.instant();
        registerEvent(new AssignmentClosedEvent(this));
    }

    public boolean isOpen() {
        return closed == null;
    }

    @NonNull
    public AssignmentResource assignResource(@NonNull ResourceId resource) {
        requireOpen();
        Optional<AssignmentResource> existingResource = assignmentResources.stream()
                .filter(withResource(resource))
                .filter(AssignmentResource::isAssigned)
                .findFirst();
        if (existingResource.isPresent()) {
            return existingResource.get();
        } else {
            AssignmentResource newResource = new AssignmentResource(this, resource);
            assignmentResources.add(newResource);
            return newResource;
        }
    }

    void updateResourceStateIfApplicable(@NonNull ResourceId resource, @NonNull ResourceState newState,
                                         @NonNull Instant stateChangedOn) {
        requireOpen();
        Optional<AssignmentResource> existingResource = assignmentResources.stream()
                .filter(withResource(resource))
                .filter(withSettableTimestampFor(newState))
                .findFirst();
        existingResource.ifPresent(ar -> ar.setTimestampFor(newState, stateChangedOn));
    }

    /**
     * Checks that this assignment is open and throws an {@link IllegalStateException} if it is not.
     */
    void requireOpen() {
        if (closed != null) {
            throw new IllegalStateException("This assignment is closed and cannot be changed");
        }
    }

    @NonNull
    public Set<AssignmentResource> getAssignedResources() {
        return Collections.emptySet(); // TODO Implement me!
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

    @NonNull
    public Set<AssignmentResource> getAssignmentResources() {
        return Collections.unmodifiableSet(assignmentResources);
    }

    @NonNull
    public AssignmentPriority getPriority() {
        return priority;
    }

    @Nullable
    public AssignmentTypeId getType() {
        return type;
    }

    @NonNull
    public String getDescription() {
        return description;
    }

    @Nullable
    public MunicipalityId getMunicipality() {
        return municipality;
    }

    @NonNull
    public String getAddress() {
        return address;
    }

    // TODO Add more methods for altering the state
}
