package net.pkhsolutions.idispatch.entity;

import org.springframework.data.domain.Persistable;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import java.io.Serializable;
import java.util.Optional;

/**
 * Base class for persistable entities.
 */
@MappedSuperclass
public abstract class AbstractEntity implements Persistable<Long>, Serializable, Cloneable {

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

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    public Optional<Object> safeClone() {
        try {
            return Optional.of(clone());
        } catch (CloneNotSupportedException ex) {
            return Optional.empty();
        }
    }
}
