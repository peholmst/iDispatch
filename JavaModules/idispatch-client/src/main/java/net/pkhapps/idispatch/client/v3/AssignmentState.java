package net.pkhapps.idispatch.client.v3;

import net.pkhapps.idispatch.client.v3.util.Color;
import net.pkhapps.idispatch.client.v3.base.DeactivatableDomainObject;
import net.pkhapps.idispatch.client.v3.base.IdentifiableDomainObject;
import net.pkhapps.idispatch.client.v3.util.MultilingualString;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.Immutable;
import java.util.Objects;

@Immutable
public class AssignmentState implements IdentifiableDomainObject<AssignmentStateId>, DeactivatableDomainObject {

    private AssignmentStateId id;
    private MultilingualString name;
    private MultilingualString description;
    private Color color;
    private boolean active;

    @SuppressWarnings("unused") // Used by GSON
    private AssignmentState() {
    }

    public AssignmentState(@Nonnull AssignmentStateId id, @Nonnull MultilingualString name,
                           @Nonnull MultilingualString description, @Nonnull Color color, boolean active) {
        this.id = Objects.requireNonNull(id, "id must not be null");
        this.name = Objects.requireNonNull(name, "name must not be null");
        this.description = Objects.requireNonNull(description, "description must not be null");
        this.color = Objects.requireNonNull(color, "color must not be null");
        this.active = active;
    }

    @Nonnull
    @Override
    public AssignmentStateId id() {
        return id;
    }

    @Nonnull
    public MultilingualString name() {
        return name;
    }

    @Nonnull
    public MultilingualString description() {
        return description;
    }

    @Nonnull
    public Color color() {
        return color;
    }

    @Override
    public boolean active() {
        return active;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AssignmentState that = (AssignmentState) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(name, that.name) &&
                Objects.equals(description, that.description) &&
                Objects.equals(color, that.color)
                && Objects.equals(active, that.active);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, description, color, active);
    }
}
