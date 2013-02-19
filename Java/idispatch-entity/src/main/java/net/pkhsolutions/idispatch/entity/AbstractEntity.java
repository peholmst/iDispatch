package net.pkhsolutions.idispatch.entity;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
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

    public static abstract class AbstractEntityBuilder<E extends AbstractEntity, B extends AbstractEntityBuilder<E, B>> {

        protected E entity;

        public AbstractEntityBuilder(Class<E> entityClass) {
            try {
                entity = entityClass.newInstance();
            } catch (InstantiationException | IllegalAccessException ex) {
                Logger.getLogger(AbstractEntity.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        public AbstractEntityBuilder(Class<E> entityClass, E original) {
            this(entityClass);
            entity.setId(original.getId());
        }

        public E build() {
            return entity;
        }

        public B clearIdentityInfo() {
            entity.setId(null);
            return (B) this;
        }

        protected static <T> T clone(T original) {
            if (original == null) {
                return null;
            } else if (original instanceof Cloneable) {
                try {
                    Method clone = original.getClass().getMethod("clone");
                    return (T) clone.invoke(original);
                } catch (NoSuchMethodException | SecurityException | IllegalAccessException | InvocationTargetException ex) {
                    throw new IllegalArgumentException("Object cannot be cloned even though it implements the Cloneable interface", ex);
                }
            } else {
                throw new IllegalArgumentException("Object is not cloneable");
            }
        }
    }
}
