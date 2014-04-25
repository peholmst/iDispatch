package net.pkhsolutions.idispatch.domain.tickets.events;

import net.pkhsolutions.idispatch.domain.tickets.Ticket;
import org.springframework.context.ApplicationEvent;

/**
 * Base class for ticket events.
 */
public abstract class TicketEvent extends ApplicationEvent {

    private final Ticket ticket;

    protected TicketEvent(Object source, Ticket ticket) {
        super(source);
        this.ticket = ticket;
    }

    public Ticket getTicket() {
        return ticket;
    }
}
