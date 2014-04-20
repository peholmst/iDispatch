package net.pkhsolutions.idispatch.domain.tickets.events;

import net.pkhsolutions.idispatch.domain.tickets.Ticket;

import java.io.Serializable;

/**
 * Base class for ticket events.
 */
public abstract class TicketEvent implements Serializable {

    private final Ticket ticket;

    protected TicketEvent(Ticket ticket) {
        this.ticket = ticket;
    }

    public Ticket getTicket() {
        return ticket;
    }
}
