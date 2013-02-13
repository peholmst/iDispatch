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

    public static abstract class AbstractEntityWithOptimisticLockingBuilder extends AbstractEntityBuilder {

        public AbstractEntityWithOptimisticLockingBuilder(Class<? extends AbstractEntityWithOptimisticLocking> entityClass) {
            super(entityClass);
        }

        @SuppressWarnings("OverridableMethodCallInConstructor")
        public AbstractEntityWithOptimisticLockingBuilder(Class<? extends AbstractEntityWithOptimisticLocking> entityClass, AbstractEntityWithOptimisticLocking original) {
            this(entityClass);
            getEntity().version = original.version;
        }

        @Override
        protected AbstractEntityWithOptimisticLocking getEntity() {
            return (AbstractEntityWithOptimisticLocking) super.getEntity();
        }
    }
}
