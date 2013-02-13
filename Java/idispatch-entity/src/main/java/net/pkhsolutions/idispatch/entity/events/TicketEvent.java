package net.pkhsolutions.idispatch.entity.events;

import net.pkhsolutions.idispatch.entity.Ticket;

/**
 * Base class for events that concern a single Ticket.
 *
 * @author Petter Holmström
 */
public abstract class TicketEvent implements java.io.Serializable {

    private final Ticket ticket;

    public TicketEvent(Ticket ticket) {
        this.ticket = ticket;
    }

    public Ticket getTicket() {
        return ticket;
    }
}
