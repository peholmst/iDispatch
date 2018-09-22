package net.pkhapps.idispatch.client.v3;

import net.pkhapps.idispatch.client.v3.base.TemporalValue;
import net.pkhapps.idispatch.client.v3.geo.GeographicLocation;
import net.pkhapps.idispatch.client.v3.type.ResourceId;
import net.pkhapps.idispatch.client.v3.type.ResourceStateId;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.Immutable;
import java.io.Serializable;
import java.util.Objects;

/**
 * Value object representing the status of a resource at some instant in time.
 */
@Immutable
@SuppressWarnings("WeakerAccess")
public class ResourceStatus implements Serializable {

    private ResourceId resource;
    private TemporalValue<ResourceStateId> state;
    private TemporalValue<GeographicLocation> location;
    private TemporalValue<AssignmentId> assignment;

    public ResourceStatus(@Nonnull ResourceId resource, @Nonnull TemporalValue<ResourceStateId> state,
                          @Nonnull TemporalValue<GeographicLocation> location,
                          @Nonnull TemporalValue<AssignmentId> assignment) {
        this.resource = Objects.requireNonNull(resource, "resource must not be null");
        this.state = Objects.requireNonNull(state, "state must not be null");
        this.location = Objects.requireNonNull(location, "location must not be null");
        this.assignment = Objects.requireNonNull(assignment, "assignment must not be null");
    }

    @Nonnull
    public ResourceId resource() {
        return resource;
    }

    @Nonnull
    public TemporalValue<ResourceStateId> state() {
        return state;
    }

    @Nonnull
    public TemporalValue<GeographicLocation> location() {
        return location;
    }

    @Nonnull
    public TemporalValue<AssignmentId> assignment() {
        return assignment;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ResourceStatus that = (ResourceStatus) o;
        return Objects.equals(resource, that.resource) &&
                Objects.equals(state, that.state) &&
                Objects.equals(location, that.location) &&
                Objects.equals(assignment, that.assignment);
    }

    @Override
    public int hashCode() {
        return Objects.hash(resource, state, location, assignment);
    }

    @Override
    public String toString() {
        return String.format("%s[resource=%s, state=%s, location=%s, assignment=%s]", getClass().getSimpleName(),
                resource, state, location, assignment);
    }
}
