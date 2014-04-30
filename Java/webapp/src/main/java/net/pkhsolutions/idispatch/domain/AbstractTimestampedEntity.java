package net.pkhsolutions.idispatch.domain;

import javax.persistence.*;
import java.util.Date;

/**
 * Base class for entities that are timestamped ("application events"). Normally, these events never change
 * once they have been persisted.
 */
@MappedSuperclass
public abstract class AbstractTimestampedEntity extends AbstractEntity {

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

    /**
     * Base class for builders of timestamped entities.
     *
     * @param <E> the entity that is being built.
     * @param <B> the builder type, to be used for chaining method calls.
     */
    public static abstract class AbstractTimestampedEntityBuilder<E extends AbstractTimestampedEntity, B extends AbstractTimestampedEntityBuilder<E, B>> extends AbstractEntityBuilder<E, B> {

        protected AbstractTimestampedEntityBuilder(Class<E> entityClass) {
            super(entityClass);
            entity.setTimestamp(new Date());
        }

        /**
         * This constructor clears the identity information and resets the timestamp.
         */
        protected AbstractTimestampedEntityBuilder(Class<E> entityClass, AbstractTimestampedEntity original) {
            super(entityClass, original);
            entity.setTimestamp(new Date());
            clearIdentityInfo();
        }

        @SuppressWarnings("unchecked")
        public B withTimestamp(Date timestamp) {
            entity.setTimestamp(timestamp);
            return (B) this;
        }
    }
}
