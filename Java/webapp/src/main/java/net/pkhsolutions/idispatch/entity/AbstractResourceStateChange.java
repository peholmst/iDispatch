package net.pkhsolutions.idispatch.entity;

import javax.persistence.*;

/**
 * Base entity that contains a state of a {@link net.pkhsolutions.idispatch.entity.Resource}.
 */
@MappedSuperclass
public abstract class AbstractResourceStateChange extends AbstractTimestampedEntity {

    public static final String PROP_STATE = "state";
    public static final String PROP_RESOURCE = "resource";
    public static final String PROP_TICKET = "ticket";

    @Enumerated(EnumType.STRING)
    @Column(name = "state", nullable = false)
    private ResourceState state = ResourceState.UNAVAILABLE;
    @ManyToOne
    @JoinColumn(name = "ticket_id")
    private Ticket ticket;

    protected AbstractResourceStateChange() {
    }

    public abstract Resource getResource();

    public ResourceState getState() {
        return state;
    }

    protected void setState(ResourceState state) {
        this.state = state;
    }

    /**
     * Returns the ticket that the resource is assigned to, or {@code null} if
     * the resource is not assigned to any ticket.
     */
    public Ticket getTicket() {
        return ticket;
    }

    protected void setTicket(Ticket ticket) {
        this.ticket = ticket;
    }
}
