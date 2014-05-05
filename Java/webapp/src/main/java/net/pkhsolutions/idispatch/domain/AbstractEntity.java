package net.pkhsolutions.idispatch.domain;

import org.springframework.data.domain.Persistable;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import java.io.Serializable;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Base class for persistable entities.
 */
@MappedSuperclass
public abstract class AbstractEntity implements Persistable<Long>, Serializable {

    public static final String PROP_ID = "id";

    @Id
    @GeneratedValue
    @Column(name = "id", nullable = false)
    private Long id;

    protected AbstractEntity() {
    }

    @Override
    public Long getId() {
        return id;
    }

    protected void setId(Long id) {
        this.id = id;
    }

    @Override
    public boolean isNew() {
        return id == null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        final AbstractEntity that = (AbstractEntity) o;

        return id != null && id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : System.identityHashCode(this);
    }

    /**
     * Base class for builders of persistable entities.
     *
     * @param <E> the entity that is being built.
     * @param <B> the builder type, to be used for chaining method calls.
     */
    public static abstract class AbstractEntityBuilder<E extends AbstractEntity, B extends AbstractEntityBuilder<E, B>> {

        protected E entity;

        protected AbstractEntityBuilder(Class<E> entityClass) {
            try {
                final Constructor<E> constructor = entityClass.getDeclaredConstructor();
                constructor.setAccessible(true);
                entity = constructor.newInstance();
            } catch (Exception ex) {
                throw new IllegalStateException("Could not create new entity instance", ex);
            }
        }

        protected AbstractEntityBuilder(Class<E> entityClass, AbstractEntity original) {
            this(entityClass);
            entity.setId(original.getId());
        }

        @SuppressWarnings("unchecked")
        protected static <T> T clone(T original) {
            if (original == null) {
                return null;
            } else if (original instanceof Cloneable) {
                try {
                    final Method clone = original.getClass().getMethod("clone");
                    return (T) clone.invoke(original);
                } catch (NoSuchMethodException | SecurityException | IllegalAccessException | InvocationTargetException ex) {
                    throw new IllegalArgumentException("Object cannot be cloned even though it implements the Cloneable interface", ex);
                }
            } else {
                throw new IllegalArgumentException("Object is not cloneable");
            }
        }

        public E build() {
            return entity;
        }

        @SuppressWarnings("unchecked")
        public B clearIdentityInfo() {
            entity.setId(null);
            return (B) this;
        }
    }
}
