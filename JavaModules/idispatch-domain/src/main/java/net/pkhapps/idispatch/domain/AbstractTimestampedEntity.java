package net.pkhapps.idispatch.domain;

import net.pkhapps.idispatch.domain.base.AbstractEntity;

import javax.persistence.*;
import java.util.Date;

/**
 * Base class for entities that are timestamped ("application events"). Normally, these events never change
 * once they have been persisted.
 */
@MappedSuperclass
public abstract class AbstractTimestampedEntity extends AbstractEntity {

    public static final String PROP_TIMESTAMP = "timestamp";

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "ts", nullable = false)
    private Date timestamp;

    protected AbstractTimestampedEntity() {
    }

    public Date getTimestamp() {
        return timestamp;
    }

    protected void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    /**
     * Sets the timestamp to the current date and time, unless a timestamp has already been set.
     */
    @PrePersist
    protected void beforePersist() {
        if (getTimestamp() == null) {
            setTimestamp(new Date());
        }
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        throw new CloneNotSupportedException("Timestamped entities cannot be cloned");
    }
}
