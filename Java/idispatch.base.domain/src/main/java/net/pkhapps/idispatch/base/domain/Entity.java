package net.pkhapps.idispatch.base.domain;

import org.springframework.data.domain.Persistable;
import org.springframework.data.util.ProxyUtils;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.Version;

/**
 * Base class for entities. You typically don't want to subclass this directly but start from either {@link LocalEntity}
 * or {@link AggregateRoot} instead. To ensure data consistency, all entities use optimistic locking (when using JPA,
 * it is not enough to use optimistic locking on the aggregate root).
 */
@MappedSuperclass
public abstract class Entity<ID> implements IdentifiableDomainObject<ID>, Persistable<ID> {

    @Version
    @Column(name = "opt_lock_version", nullable = false)
    private Long optLockVersion;

    public Long getOptLockVersion() {
        return optLockVersion;
    }

    protected void setOptLockVersion(Long optLockVersion) {
        this.optLockVersion = optLockVersion;
    }

    @Override
    public boolean isNew() {
        return getId() == null;
    }

    @Override
    public boolean equals(Object obj) {
        if (null == obj) {
            return false;
        } else if (this == obj) {
            return true;
        } else if (!this.getClass().equals(ProxyUtils.getUserClass(obj))) {
            return false;
        } else {
            var that = (Entity) obj;
            return null != this.getId() && this.getId().equals(that.getId());
        }
    }

    @Override
    public int hashCode() {
        return ProxyUtils.getUserClass(this).hashCode() + (null == this.getId() ? 0 : this.getId().hashCode() * 31);
    }
}
