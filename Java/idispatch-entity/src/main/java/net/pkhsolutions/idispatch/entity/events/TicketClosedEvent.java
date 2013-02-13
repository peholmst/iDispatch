package net.pkhsolutions.idispatch.entity.events;

import net.pkhsolutions.idispatch.entity.Ticket;

/**
 * Event fired when an existing Ticket is closed.
 *
 * @author Petter Holmström
 */
public class TicketClosedEvent extends TicketEvent {

    public TicketClosedEvent(Ticket ticket) {
        super(ticket);
    }
}
