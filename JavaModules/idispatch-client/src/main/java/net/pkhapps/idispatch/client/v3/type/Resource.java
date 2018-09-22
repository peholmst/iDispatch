package net.pkhapps.idispatch.client.v3.type;

import net.pkhapps.idispatch.client.v3.base.DeactivatableDomainObject;
import net.pkhapps.idispatch.client.v3.base.IdentifiableDomainObject;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.Immutable;
import java.util.Objects;

/**
 * Identifiable value object representing a resource.
 */
@Immutable
@SuppressWarnings("WeakerAccess")
public class Resource implements IdentifiableDomainObject<ResourceId>, DeactivatableDomainObject {

    private ResourceId id;
    private ResourceType type;
    private String designation;
    private Station stationedAt;
    private boolean active;

    public Resource(@Nonnull ResourceId id, @Nonnull ResourceType type, @Nonnull String designation,
                    @Nonnull Station stationedAt, boolean active) {
        this.id = Objects.requireNonNull(id, "id must not be null");
        this.type = Objects.requireNonNull(type, "type must not be null");
        this.designation = Objects.requireNonNull(designation, "designation must not be null");
        this.stationedAt = Objects.requireNonNull(stationedAt, "stationedAt must not be null");
        this.active = active;
    }

    @Nonnull
    @Override
    public ResourceId id() {
        return id;
    }

    @Nonnull
    public ResourceType type() {
        return type;
    }

    @Nonnull
    public String designation() {
        return designation;
    }

    @Nonnull
    public Station stationedAt() {
        return stationedAt;
    }

    @Override
    public boolean active() {
        return active;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Resource resource = (Resource) o;
        return active == resource.active &&
                Objects.equals(id, resource.id) &&
                Objects.equals(type, resource.type) &&
                Objects.equals(designation, resource.designation) &&
                Objects.equals(stationedAt, resource.stationedAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, type, designation, stationedAt, active);
    }

    @Override
    public String toString() {
        return String.format("%s[id=%s, designation=%s, active=%s]", getClass().getSimpleName(), id, designation,
                active);
    }
}
