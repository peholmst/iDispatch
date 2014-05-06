package net.pkhsolutions.idispatch.domain.dispatch;

import net.pkhsolutions.idispatch.domain.AbstractTimestampedEntity;
import net.pkhsolutions.idispatch.domain.Municipality;
import net.pkhsolutions.idispatch.domain.resources.Resource;
import net.pkhsolutions.idispatch.domain.tickets.Ticket;
import net.pkhsolutions.idispatch.domain.tickets.TicketType;
import net.pkhsolutions.idispatch.domain.tickets.TicketUrgency;

import javax.persistence.*;
import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.collect.Sets.newHashSet;
import static java.util.Collections.unmodifiableSet;

@Entity
@Table(name = "dispatch_notifications")
public class DispatchNotification extends AbstractTimestampedEntity {

    @ManyToMany
    @JoinTable(name = "dispatched_notification_resources",
            joinColumns = @JoinColumn(name = "dispatch_notification_id"),
            inverseJoinColumns = @JoinColumn(name = "resource_id"))
    private Set<Resource> resources;

    @ManyToMany
    @JoinTable(name = "dispatch_notification_destinations",
            joinColumns = @JoinColumn(name = "dispatch_notification_id"),
            inverseJoinColumns = @JoinColumn(name = "destination_id"))
    private Set<Destination> destinations;

    @ManyToOne(optional = false)
    @JoinColumn(name = "ticket_id", nullable = false)
    private Ticket ticket;

    @ManyToOne(optional = false)
    @JoinColumn(name = "ticket_municipality_id", nullable = false)
    private Municipality municipality;

    @Column(name = "ticket_address", nullable = false)
    private String address;

    @ManyToOne(optional = false)
    @JoinColumn(name = "ticket_type_id", nullable = false)
    private TicketType ticketType;

    @Enumerated(EnumType.STRING)
    @Column(name = "ticket_urgency", nullable = false)
    private TicketUrgency urgency;

    @Column(name = "ticket_description", nullable = false)
    private String description;

    protected DispatchNotification() {
    }

    public DispatchNotification(Ticket ticket, Collection<Resource> resources, Set<Destination> destinations) {
        this.resources = newHashSet(checkNotNull(resources));
        this.destinations = newHashSet(checkNotNull(destinations));
        this.ticket = checkNotNull(ticket);
        municipality = ticket.getMunicipality();
        address = ticket.getAddress();
        ticketType = ticket.getType();
        urgency = ticket.getUrgency();
        description = ticket.getDescription();
    }

    public Set<Resource> getResources() {
        return unmodifiableSet(resources);
    }

    public Set<Destination> getDestinations() {
        return unmodifiableSet(destinations);
    }

    public <D extends Destination> Set<D> getDestinationsOfType(Class<D> type) {
        return destinations.stream().filter(type::isInstance).map(type::cast).collect(Collectors.toSet());
    }

    public Ticket getTicket() {
        return ticket;
    }

    public Municipality getMunicipality() {
        return municipality;
    }

    public String getAddress() {
        return address;
    }

    public TicketType getTicketType() {
        return ticketType;
    }

    public TicketUrgency getUrgency() {
        return urgency;
    }

    public String getDescription() {
        return description;
    }
}
