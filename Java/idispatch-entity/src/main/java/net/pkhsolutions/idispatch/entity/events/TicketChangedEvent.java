package net.pkhsolutions.idispatch.entity.events;

import net.pkhsolutions.idispatch.entity.Ticket;

import java.io.Serializable;

public class TicketChangedEvent implements Serializable {
    private final Ticket ticket;

    public TicketChangedEvent(Ticket ticket) {
        this.ticket = ticket;
    }

    public Ticket getTicket() {
        return ticket;
    }
}
