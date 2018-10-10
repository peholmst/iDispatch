package net.pkhapps.idispatch.cad.domain.model.resource;

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
    private String nameSv;
    private String nameFi;

    @UsedByPersistenceFramework
    private ResourceType() {
    }

    public ResourceType(@Nonnull String nameSv, @Nonnull String nameFi) {
        setNameSv(nameSv);
        setNameFi(nameFi);
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
    public String nameFi() {
        return nameFi;
    }

    private void setNameFi(@Nonnull String nameFi) {
        this.nameFi = Objects.requireNonNull(nameFi, "nameFi must not be null");
    }

    @Nonnull
    public String nameSv() {
        return nameSv;
    }

    private void setNameSv(@Nonnull String nameSv) {
        this.nameSv = Objects.requireNonNull(nameSv, "nameSv must not be null");
    }
}
