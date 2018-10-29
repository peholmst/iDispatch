package net.pkhapps.idispatch.cad.domain.model.resource;

import net.pkhapps.idispatch.cad.domain.model.common.MultilingualString;
import net.pkhapps.idispatch.domain.support.AggregateRoot;
import net.pkhapps.idispatch.domain.support.ConcurrencySafeDomainObject;
import net.pkhapps.idispatch.domain.support.DeactivatableDomainObject;
import net.pkhapps.idispatch.domain.support.UsedByPersistenceFramework;

import javax.annotation.Nonnull;
import java.util.Objects;

/**
 * Aggregate root that describes the type of a {@link Resource}.
 */
public class ResourceType extends AggregateRoot<ResourceTypeId> implements ConcurrencySafeDomainObject,
        DeactivatableDomainObject {

    @UsedByPersistenceFramework
    private long version;
    private boolean active;
    private MultilingualString name;

    @UsedByPersistenceFramework
    protected ResourceType() {
    }

    public ResourceType(@Nonnull ResourceTypeId id) { // TODO name
        super(id);
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
}
