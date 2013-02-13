package net.pkhsolutions.idispatch.entity;

import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

@MappedSuperclass
public class AbstractEntity implements Serializable {

    @Id
    @GeneratedValue
    private Long id;

    protected AbstractEntity() {
    }

    public Long getId() {
        return id;
    }

    protected void setId(Long id) {
        this.id = id;
    }

    public boolean isPersistent() {
        return id != null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        AbstractEntity that = (AbstractEntity) o;

        return id != null && id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : System.identityHashCode(this);
    }

    public static abstract class AbstractEntityBuilder {

        private AbstractEntity entity;

        public AbstractEntityBuilder(Class<? extends AbstractEntity> entityClass) {
            try {
                entity = entityClass.newInstance();
            } catch (InstantiationException | IllegalAccessException ex) {
                Logger.getLogger(AbstractEntity.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        public AbstractEntityBuilder(Class<? extends AbstractEntity> entityClass, AbstractEntity original) {
            this(entityClass);
            entity.id = original.id;
        }

        public AbstractEntity build() {
            return entity;
        }

        protected AbstractEntity getEntity() {
            return entity;
        }
    }
}
