package net.pkhapps.idispatch.client.v3.type;

import net.pkhapps.idispatch.client.v3.base.DeactivatableDomainObject;
import net.pkhapps.idispatch.client.v3.base.IdentifiableDomainObject;
import net.pkhapps.idispatch.client.v3.util.MultilingualString;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.Immutable;
import java.util.Objects;

/**
 * Identifiable value object representing a municipality.
 */
@Immutable
@SuppressWarnings("WeakerAccess")
public class Municipality implements IdentifiableDomainObject<MunicipalityId>, DeactivatableDomainObject {

    private MunicipalityId id;
    private MultilingualString name;
    private boolean active;

    public Municipality(@Nonnull MunicipalityId id, @Nonnull MultilingualString name, boolean active) {
        this.id = Objects.requireNonNull(id, "id must not be null");
        this.name = Objects.requireNonNull(name, "name must not be null");
        this.active = active;
    }

    @Nonnull
    @Override
    public MunicipalityId id() {
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
        Municipality that = (Municipality) o;
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
        return String.format("%s[id=%s, name=%s, active=%s]", getClass().getSimpleName(), id, name, active);
    }
}
