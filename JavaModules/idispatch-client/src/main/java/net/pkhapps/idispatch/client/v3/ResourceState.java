package net.pkhapps.idispatch.client.v3;

import net.pkhapps.idispatch.client.v3.base.DeactivatableDomainObject;
import net.pkhapps.idispatch.client.v3.base.IdentifiableDomainObject;
import net.pkhapps.idispatch.client.v3.util.Color;
import net.pkhapps.idispatch.client.v3.util.MultilingualString;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.Immutable;
import java.util.Objects;
import java.util.Set;

/**
 * Identifiable value object representing a resource state.
 */
@Immutable
@SuppressWarnings("WeakerAccess")
public class ResourceState implements IdentifiableDomainObject<ResourceStateId>, DeactivatableDomainObject {

    private ResourceStateId id;
    private MultilingualString name;
    private Color color;
    private boolean assignableByResource;
    private Set<ResourceStateId> validTransitionsByResource;
    private boolean assignableByDispatcher;
    private Set<ResourceStateId> validTransitionsByDispatcher;
    private boolean requiresAssignment;
    private boolean active;

    public ResourceState(@Nonnull ResourceStateId id, @Nonnull MultilingualString name, @Nonnull Color color,
                         boolean assignableByResource, @Nonnull Set<ResourceStateId> validTransitionsByResource,
                         boolean assignableByDispatcher, @Nonnull Set<ResourceStateId> validTransitionsByDispatcher,
                         boolean requiresAssignment, boolean active) {
        this.id = Objects.requireNonNull(id, "id must not be null");
        this.name = Objects.requireNonNull(name, "name must not be null");
        this.color = Objects.requireNonNull(color, "color must not be null");
        this.assignableByResource = assignableByResource;
        this.validTransitionsByResource = Set.copyOf(validTransitionsByResource);
        this.assignableByDispatcher = assignableByDispatcher;
        this.validTransitionsByDispatcher = Set.copyOf(validTransitionsByDispatcher);
        this.requiresAssignment = requiresAssignment;
        this.active = active;
    }

    @Nonnull
    @Override
    public ResourceStateId id() {
        return id;
    }

    @Nonnull
    public MultilingualString name() {
        return name;
    }

    @Nonnull
    public Color color() {
        return color;
    }

    public boolean assignableByResource() {
        return assignableByResource;
    }

    @Nonnull
    public Set<ResourceStateId> validTransitionsByResource() {
        return validTransitionsByResource; // Unmodifiable
    }

    public boolean assignableByDispatcher() {
        return assignableByDispatcher;
    }

    @Nonnull
    public Set<ResourceStateId> validTransitionsByDispatcher() {
        return validTransitionsByDispatcher; // Unmodifiable
    }

    public boolean requiresAssignment() {
        return requiresAssignment;
    }

    @Override
    public boolean active() {
        return active;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ResourceState that = (ResourceState) o;
        return assignableByResource == that.assignableByResource &&
                assignableByDispatcher == that.assignableByDispatcher &&
                active == that.active &&
                requiresAssignment == that.requiresAssignment &&
                Objects.equals(id, that.id) &&
                Objects.equals(name, that.name) &&
                Objects.equals(color, that.color) &&
                Objects.equals(validTransitionsByResource, that.validTransitionsByResource) &&
                Objects.equals(validTransitionsByDispatcher, that.validTransitionsByDispatcher);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, color, assignableByResource, validTransitionsByResource, assignableByDispatcher,
                validTransitionsByDispatcher, requiresAssignment, active);
    }

    @Override
    public String toString() {
        return String.format("%s[id=%s, name=%s, active=%s]", getClass().getSimpleName(), id, name, active);
    }
}
