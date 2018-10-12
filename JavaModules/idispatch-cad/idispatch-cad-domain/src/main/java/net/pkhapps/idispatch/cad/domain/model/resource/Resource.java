package net.pkhapps.idispatch.cad.domain.model.resource;

import net.pkhapps.idispatch.cad.domain.model.station.StationId;
import net.pkhapps.idispatch.domain.support.AggregateRoot;
import net.pkhapps.idispatch.domain.support.ConcurrencySafeDomainObject;
import net.pkhapps.idispatch.domain.support.DeactivatableDomainObject;
import net.pkhapps.idispatch.domain.support.UsedByPersistenceFramework;

import javax.annotation.Nonnull;
import java.util.Objects;

/**
 * Aggregate root that represents a Resource, such as a particular fire engine or ambulance.
 */
public class Resource extends AggregateRoot<ResourceId> implements ConcurrencySafeDomainObject,
        DeactivatableDomainObject {

    @UsedByPersistenceFramework
    private long version;
    private boolean active;
    private ResourceTypeId type;
    private String designation;
    private StationId stationedAt;

    @UsedByPersistenceFramework
    private Resource() {
    }

    public Resource(@Nonnull ResourceId id, @Nonnull ResourceTypeId type, @Nonnull String designation,
                    @Nonnull StationId stationedAt) {
        super(id);
        setType(type);
        setDesignation(designation);
        setStationedAt(stationedAt);
        active = true;
    }

    @Override
    public long version() {
        return version;
    }

    @Override
    public boolean active() {
        return active;
    }

    @Override
    public void deactivate() {
        active = false;
    }

    @Override
    public void activate() {
        active = true;
    }

    @Nonnull
    public ResourceTypeId type() {
        return type;
    }

    private void setType(@Nonnull ResourceTypeId type) {
        this.type = Objects.requireNonNull(type, "type must not be null");
    }

    @Nonnull
    public String designation() {
        return designation;
    }

    private void setDesignation(@Nonnull String designation) {
        this.designation = Objects.requireNonNull(designation, "designation must not be null");
    }

    @Nonnull
    public StationId stationedAt() {
        return stationedAt;
    }

    private void setStationedAt(@Nonnull StationId stationedAt) {
        this.stationedAt = Objects.requireNonNull(stationedAt, "stationedAt must not be null");
    }
}
