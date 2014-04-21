package net.pkhsolutions.idispatch.domain;

import javax.persistence.MappedSuperclass;
import javax.persistence.Version;

/**
 * Base class for persistable entities that use optimistic locking to prevent accidental overwrites.
 */
@MappedSuperclass
public class AbstractLockableEntity extends AbstractEntity {

    @Version
    private Long version;

    protected AbstractLockableEntity() {
    }

    public Long getVersion() {
        return version;
    }

    protected void setVersion(Long version) {
        this.version = version;
    }

    /**
     * Base class for builders of persistable entities that use optimistic locking.
     * @param <E> the entity that is being built.
     * @param <B> the builder type, to be used for chaining method calls.
     */
    public static abstract class AbstractLockableEntityBuilder<E extends AbstractLockableEntity, B extends AbstractLockableEntityBuilder<E, B>> extends AbstractEntityBuilder<E, B> {

        public AbstractLockableEntityBuilder(Class<E> entityClass) {
            super(entityClass);
        }

        public AbstractLockableEntityBuilder(Class<E> entityClass, AbstractLockableEntity original) {
            super(entityClass, original);
            entity.setVersion(original.getVersion());
        }

        @Override
        public B clearIdentityInfo() {
            entity.setVersion(null);
            return super.clearIdentityInfo();
        }
    }
}
