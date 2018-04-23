package net.pkhapps.idispatch.domain.status;

import net.pkhapps.idispatch.domain.resource.ResourceId;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

import java.time.Instant;
import java.util.Objects;

/**
 * Domain event published when the state of a resource is changed.
 */
public class ResourceStateChangedEvent {

    private final ResourceId resource;
    private final ResourceState oldState;
    private final ResourceState newState;
    private final Instant timestamp;

    public ResourceStateChangedEvent(@NonNull ResourceId resource,
                                     @Nullable ResourceState oldState,
                                     @NonNull ResourceState newState,
                                     @NonNull Instant timestamp) {
        this.resource = Objects.requireNonNull(resource);
        this.oldState = oldState;
        this.newState = Objects.requireNonNull(newState);
        this.timestamp = timestamp;
    }

    @NonNull
    public ResourceId getResource() {
        return resource;
    }

    @Nullable
    public ResourceState getOldState() {
        return oldState;
    }

    @NonNull
    public ResourceState getNewState() {
        return newState;
    }

    @NonNull
    public Instant getTimestamp() {
        return timestamp;
    }
}
