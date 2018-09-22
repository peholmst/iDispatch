package net.pkhapps.idispatch.client.v3.type;

import net.pkhapps.idispatch.client.v3.base.DeactivatableDomainObject;
import net.pkhapps.idispatch.client.v3.base.IdentifiableDomainObject;
import net.pkhapps.idispatch.client.v3.geo.GeographicLocation;
import net.pkhapps.idispatch.client.v3.util.MultilingualString;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.Immutable;
import java.util.Objects;

/**
 * Identifiable value object representing a station.
 */
@Immutable
@SuppressWarnings("WeakerAccess")
public class Station implements IdentifiableDomainObject<StationId>, DeactivatableDomainObject {

    private StationId id;
    private MultilingualString name;
    private GeographicLocation location;
    private boolean active;

    public Station(@Nonnull StationId id, @Nonnull MultilingualString name, @Nonnull GeographicLocation location,
                   boolean active) {
        this.id = Objects.requireNonNull(id, "id must not be null");
        this.name = Objects.requireNonNull(name, "name must not be null");
        this.location = Objects.requireNonNull(location, "location must not be null");
        this.active = active;
    }

    @Nonnull
    @Override
    public StationId id() {
        return id;
    }

    @Nonnull
    public MultilingualString name() {
        return name;
    }

    @Nonnull
    public GeographicLocation location() {
        return location;
    }

    @Override
    public boolean active() {
        return active;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Station station = (Station) o;
        return active == station.active &&
                Objects.equals(id, station.id) &&
                Objects.equals(name, station.name) &&
                Objects.equals(location, station.location);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, location, active);
    }

    @Override
    public String toString() {
        return String.format("%s[id=%s, active=%s]", getClass().getSimpleName(), id, active);
    }
}
