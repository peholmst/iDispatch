package net.pkhsolutions.idispatch.domain.resources;

import net.pkhsolutions.idispatch.domain.AbstractEntity;
import net.pkhsolutions.idispatch.domain.tickets.Ticket;

import javax.persistence.*;
import java.util.Date;

/**
 * Entity that keeps track of the different status change timestamps of a resource that concern
 * a specific ticket.
 */
@Entity
@Table(name = "ticket_resources")
public class TicketResource extends AbstractEntity {

    @ManyToOne(optional = false)
    @JoinColumn(name = "ticket_id", nullable = false)
    private Ticket ticket;
    @ManyToOne(optional = false)
    @JoinColumn(name = "resource_id", nullable = false)
    private Resource resource;
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "ts_assigned")
    private Date assigned;
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "ts_dispatched")
    private Date dispatched;
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "ts_en_route")
    private Date enRoute;
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "ts_on_scene")
    private Date onScene;
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "ts_detached")
    private Date detached;
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "ts_at_station")
    private Date atStation;

    protected TicketResource() {
    }

    public Ticket getTicket() {
        return ticket;
    }

    public Resource getResource() {
        return resource;
    }

    public Date getAssigned() {
        return assigned;
    }

    public Date getDispatched() {
        return dispatched;
    }

    public Date getEnRoute() {
        return enRoute;
    }

    public Date getOnScene() {
        return onScene;
    }

    public Date getDetached() {
        return detached;
    }

    public Date getAtStation() {
        return atStation;
    }

    /**
     * Builder for creating instances of {@link net.pkhsolutions.idispatch.domain.resources.TicketResource}.
     */
    public static final class Builder extends AbstractEntityBuilder<TicketResource, Builder> {

        public Builder(Resource resource, Ticket ticket) {
            super(TicketResource.class);
            entity.resource = resource;
            entity.ticket = ticket;
        }

        public Builder(TicketResource original) {
            super(TicketResource.class, original);
            entity.resource = original.resource;
            entity.ticket = original.ticket;
            entity.assigned = original.assigned;
            entity.dispatched = original.dispatched;
            entity.enRoute = original.enRoute;
            entity.onScene = original.onScene;
            entity.detached = original.detached;
            entity.atStation = original.atStation;
        }

        public Builder withAssigned(Date assigned) {
            entity.assigned = assigned;
            return this;
        }

        public Builder withDispatched(Date dispatched) {
            entity.dispatched = dispatched;
            return this;
        }

        public Builder withEnRoute(Date enRoute) {
            entity.enRoute = enRoute;
            return this;
        }

        public Builder withOnScene(Date onScene) {
            entity.onScene = onScene;
            return this;
        }

        public Builder withDetached(Date detached) {
            entity.detached = detached;
            return this;
        }

        public Builder withAtStation(Date atStation) {
            entity.atStation = atStation;
            return this;
        }
    }
}
