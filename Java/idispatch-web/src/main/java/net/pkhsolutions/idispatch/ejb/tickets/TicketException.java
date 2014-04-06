package net.pkhsolutions.idispatch.ejb.tickets;

import net.pkhsolutions.idispatch.entity.Ticket;

/**
 * Base class for exceptions that are associated with a single ticket.
 *
 * @author Petter Holmström
 */
public abstract class TicketException extends Exception {

    private final Ticket ticket;

    public TicketException(Ticket ticket) {
        this.ticket = ticket;
    }

    public Ticket getTicket() {
        return ticket;
    }
}
