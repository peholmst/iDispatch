package net.pkhsolutions.idispatch.entity;

import javax.persistence.*;
import java.util.Calendar;

@MappedSuperclass
public abstract class AbstractResourceStatus extends AbstractEntity {

    @Enumerated
    private ResourceState resourceState;
    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false)
    private Calendar stateChangeTimestamp;
    @ManyToOne
    private Ticket ticket;

    protected AbstractResourceStatus() {
    }

    public abstract Resource getResource();

    protected abstract void setResource(Resource resource);

    public ResourceState getResourceState() {
        return resourceState;
    }

    protected void setResourceState(ResourceState resourceState) {
        this.resourceState = resourceState;
    }

    public Calendar getStateChangeTimestamp() {
        return (Calendar) stateChangeTimestamp.clone();
    }

    protected void setStateChangeTimestamp(Calendar stateChangeTimestamp) {
        this.stateChangeTimestamp = stateChangeTimestamp;
    }

    /**
     * Returns the ticket that the resource is assigned to, or {@code null} if
     * the resource is not assigned at the moment.
     */
    public Ticket getTicket() {
        return ticket;
    }

    protected void setTicket(Ticket ticket) {
        this.ticket = ticket;
    }

    public static abstract class AbstractResourceStatusBuilder<E extends AbstractResourceStatus, B extends AbstractResourceStatusBuilder<E, B>> extends AbstractEntityBuilder<E, B> {

        public AbstractResourceStatusBuilder(Class<E> entityClass) {
            super(entityClass);
        }

        public AbstractResourceStatusBuilder(Class<E> entityClass, AbstractResourceStatus original) {
            super(entityClass, original);
            entity.setResource(original.getResource());
            entity.setResourceState(original.getResourceState());
            entity.setStateChangeTimestamp(original.getStateChangeTimestamp());
            entity.setTicket(original.getTicket());
        }

        public B withResource(Resource resource) {
            entity.setResource(resource);
            return (B) this;
        }

        public B withState(ResourceState state) {
            entity.setResourceState(state);
            return (B) this;
        }

        public B withTimestamp(Calendar timestamp) {
            entity.setStateChangeTimestamp(timestamp);
            return (B) this;
        }

        public B withTicket(Ticket ticket) {
            entity.setTicket(ticket);
            return (B) this;
        }
    }
}
