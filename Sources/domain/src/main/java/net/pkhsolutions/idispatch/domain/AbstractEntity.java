/*
 * TODO Decide which license to use!
 */
package net.pkhsolutions.idispatch.domain;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Version;

/**
 * Base class for entities.
 *
 * @author Petter Holmström
 * @since 1.0
 */
public abstract class AbstractEntity<T extends AbstractEntity<T>> implements java.io.Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Version
    private Long optLockVersion;

    public Long getId() {
        return id;
    }

    public Long getOptLockVersion() {
        return optLockVersion;
    }

    /**
     * Checks if this entity has the same identity as the specified entity.
     *
     * @param entity the entity to compare to, must not be {@code null}.
     * @return true if the identities are equal, false otherwise.
     */
    public boolean hasSameIdentity(T entity) {
        return entity.getId() != null && getId() != null && getId().equals(entity.getId());
    }

    @Override
    public boolean equals(Object o) {
        if (o != null && o != this && getClass().isAssignableFrom(o.getClass())) {
            return hasSameIdentity((T) o);
        } else {
            return super.equals(o);
        }
    }

    @Override
    public int hashCode() {
        if (id == null) {
            return super.hashCode();
        } else {
            return id.hashCode();
        }
    }
}
