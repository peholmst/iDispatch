package net.pkhsolutions.idispatch.entity.events;

import net.pkhsolutions.idispatch.entity.Ticket;

/**
 * Event fired when a new Ticket is opened.
 *
 * @author Petter Holmström
 */
public class TicketOpenedEvent extends TicketEvent {

    public TicketOpenedEvent(Ticket ticket) {
        super(ticket);
    }
}
