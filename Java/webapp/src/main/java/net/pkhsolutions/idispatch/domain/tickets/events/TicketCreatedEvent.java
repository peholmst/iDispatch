package net.pkhsolutions.idispatch.domain.tickets.events;

import net.pkhsolutions.idispatch.domain.tickets.Ticket;

/**
 * Event published when a new ticket is created.
 */
public class TicketCreatedEvent extends TicketEvent {
    public TicketCreatedEvent(Object source, Ticket ticket) {
        super(source, ticket);
    }
}
