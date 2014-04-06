package net.pkhsolutions.idispatch.entity;

import javax.persistence.MappedSuperclass;
import javax.persistence.Version;

@MappedSuperclass
public class AbstractEntityWithOptimisticLocking extends AbstractEntity {

    @Version
    private Long version;

    protected AbstractEntityWithOptimisticLocking() {
    }

    public Long getVersion() {
        return version;
    }

    protected void setVersion(Long version) {
        this.version = version;
    }

    public static abstract class AbstractEntityWithOptimisticLockingBuilder<E extends AbstractEntityWithOptimisticLocking, B extends AbstractEntityWithOptimisticLockingBuilder<E, B>> extends AbstractEntityBuilder<E, B> {

        public AbstractEntityWithOptimisticLockingBuilder(Class<E> entityClass) {
            super(entityClass);
        }

        public AbstractEntityWithOptimisticLockingBuilder(Class<E> entityClass, AbstractEntityWithOptimisticLocking original) {
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
