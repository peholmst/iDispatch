package net.pkhsolutions.idispatch.domain.resources;

import net.pkhsolutions.idispatch.domain.AbstractTimestampedEntity;
import net.pkhsolutions.idispatch.domain.tickets.Ticket;

import javax.persistence.*;

import static com.google.common.base.Objects.firstNonNull;

/**
 * Entity that represents the event when the state of a {@link net.pkhsolutions.idispatch.domain.resources.Resource} changes.
 */
@Entity
@Table(name = "resource_state_changes")
public class ResourceStateChange extends AbstractTimestampedEntity {

    public static final String PROP_STATE = "state";
    public static final String PROP_RESOURCE = "resource";
    public static final String PROP_TICKET = "ticket";

    @Enumerated(EnumType.STRING)
    @Column(name = "state", nullable = false)
    private ResourceState state = ResourceState.UNAVAILABLE;
    @ManyToOne(optional = false)
    @JoinColumn(name = "resource_id", nullable = false)
    private Resource resource;
    @ManyToOne
    @JoinColumn(name = "ticket_id")
    private Ticket ticket;

    protected ResourceStateChange() {
    }

    public Resource getResource() {
        return resource;
    }

    public ResourceState getState() {
        return state;
    }

    /**
     * Returns the ticket that the resource is assigned to, or {@code null} if
     * the resource is not assigned to any ticket.
     */
    public Ticket getTicket() {
        return ticket;
    }

    /**
     * Builder for creating instances of {@link net.pkhsolutions.idispatch.domain.resources.ResourceStateChange}. Please note
     * that state change events cannot be updated once they have been created.
     */
    public static final class Builder extends AbstractTimestampedEntityBuilder<ResourceStateChange, Builder> {

        public Builder() {
            super(ResourceStateChange.class);
        }

        public Builder(ResourceStateChange original) {
            super(ResourceStateChange.class, original);
            entity.resource = original.resource;
            entity.ticket = original.ticket;
            entity.state = original.state;
        }

        public Builder withResource(Resource resource) {
            entity.resource = resource;
            return this;
        }

        public Builder withState(ResourceState state) {
            entity.state = firstNonNull(state, ResourceState.UNAVAILABLE);
            return this;
        }

        public Builder withTicket(Ticket ticket) {
            entity.ticket = ticket;
            return this;
        }
    }
}
