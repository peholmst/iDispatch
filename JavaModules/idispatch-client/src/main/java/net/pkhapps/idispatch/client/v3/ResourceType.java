package net.pkhapps.idispatch.client.v3;

import net.pkhapps.idispatch.client.v3.base.DeactivatableDomainObject;
import net.pkhapps.idispatch.client.v3.base.IdentifiableDomainObject;
import net.pkhapps.idispatch.client.v3.util.MultilingualString;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.Immutable;
import java.util.Objects;

/**
 * Identifiable value object representing a resource type.
 */
@Immutable
@SuppressWarnings("WeakerAccess")
public class ResourceType implements IdentifiableDomainObject<ResourceTypeId>, DeactivatableDomainObject {

    private ResourceTypeId id;
    private MultilingualString name;
    private boolean active;

    public ResourceType(@Nonnull ResourceTypeId id, @Nonnull MultilingualString name, boolean active) {
        this.id = Objects.requireNonNull(id, "id must not be null");
        this.name = Objects.requireNonNull(name, "name must not be null");
        this.active = active;
    }

    @Nonnull
    @Override
    public ResourceTypeId id() {
        return id;
    }

    @Nonnull
    public MultilingualString name() {
        return name;
    }

    @Override
    public boolean active() {
        return active;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ResourceType that = (ResourceType) o;
        return active == that.active &&
                Objects.equals(id, that.id) &&
                Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, active);
    }

    @Override
    public String toString() {
        return String.format("%s[id=%s, active=%s]", getClass().getSimpleName(), id, active);
    }
}
