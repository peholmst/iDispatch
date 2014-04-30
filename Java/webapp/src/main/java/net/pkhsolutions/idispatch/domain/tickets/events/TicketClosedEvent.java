package net.pkhsolutions.idispatch.domain.tickets.events;

import net.pkhsolutions.idispatch.domain.tickets.Ticket;

/**
 * Event published when an open ticket is closed.
 */
public class TicketClosedEvent extends TicketEvent {

    public TicketClosedEvent(Object source, Ticket ticket) {
        super(source, ticket);
    }
}
