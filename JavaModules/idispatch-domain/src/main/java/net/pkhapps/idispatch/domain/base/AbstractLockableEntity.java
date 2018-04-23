package net.pkhapps.idispatch.domain.base;

import net.pkhapps.idispatch.domain.base.AbstractEntity;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.Version;

/**
 * Base class for persistable entities that use optimistic locking to prevent accidental overwrites.
 */
@MappedSuperclass
@Deprecated
public abstract class AbstractLockableEntity extends AbstractEntity {

    public static final String PROP_VERSION = "version";

    @Version
    @Column(name = "version", nullable = false)
    private Long version;

    protected AbstractLockableEntity() {
    }

    public Long getVersion() {
        return version;
    }

    protected void setVersion(Long version) {
        this.version = version;
    }
}
