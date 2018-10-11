package net.pkhapps.idispatch.domain.status;

import net.pkhapps.idispatch.domain.base.AbstractAggregateRoot;
import net.pkhapps.idispatch.domain.resource.Resource;
import net.pkhapps.idispatch.domain.resource.ResourceId;
import org.springframework.lang.NonNull;

import javax.persistence.*;
import java.time.Clock;
import java.time.Instant;

/**
 * Aggregate root that contains the current state of a {@link Resource}.
 */
@Entity
@Table(name = "resource_status", indexes = @Index(columnList = "ts"))
public class ResourceStatus extends AbstractAggregateRoot<ResourceStatusId> {

    @Column(name = "resource_id", nullable = false, unique = true)
    private ResourceId resource;

    @Enumerated(EnumType.STRING)
    @Column(name = "current_state", nullable = false)
    private ResourceState currentState;

    @Column(name = "ts", nullable = false)
    private Instant stateChangedOn;

    @Column(name = "available", nullable = false)
    private boolean available;

    @SuppressWarnings("unused")
    private ResourceStatus() {
        // Used by JPA only.
    }

    public ResourceStatus(@NonNull ResourceId resource,
                          @NonNull ResourceState firstState,
                          @NonNull Clock clock) {
        this.resource = resource;
    }

    public final void changeState(@NonNull ResourceState newState, @NonNull Clock clock) {
        var oldState = currentState;
        currentState = newState;
        stateChangedOn = clock.instant();
        available = newState.isAvailableForNewAssignments();
        registerSingletonEvent(new ResourceStateChangedEvent(resource, oldState, newState, stateChangedOn));
    }

    @NonNull
    public ResourceId getResource() {
        return resource;
    }

    @NonNull
    public ResourceState getCurrentState() {
        return currentState;
    }

    @NonNull
    public Instant getStateChangedOn() {
        return stateChangedOn;
    }

    /**
     * Returns whether the resource is currently available for new assignments.
     */
    public boolean isAvailable() {
        return available;
    }
}
