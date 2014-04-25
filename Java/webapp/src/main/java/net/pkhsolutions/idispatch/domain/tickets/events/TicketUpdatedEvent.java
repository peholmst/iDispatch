package net.pkhsolutions.idispatch.domain.tickets.events;

import net.pkhsolutions.idispatch.domain.tickets.Ticket;

/**
 * Event published when an existing ticket is updated.
 */
public class TicketUpdatedEvent extends TicketEvent {
    public TicketUpdatedEvent(Object source, Ticket ticket) {
        super(source, ticket);
    }
}
