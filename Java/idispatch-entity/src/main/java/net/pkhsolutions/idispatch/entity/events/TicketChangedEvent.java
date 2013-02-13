package net.pkhsolutions.idispatch.entity.events;

import net.pkhsolutions.idispatch.entity.Ticket;

/**
 * Event fired when an existing Ticket is changed.
 *
 * @author Petter Holmström
 */
public class TicketChangedEvent extends TicketEvent {

    public TicketChangedEvent(Ticket ticket) {
        super(ticket);
    }
}