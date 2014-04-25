package net.pkhsolutions.idispatch.common.ui.tickets;

import net.pkhsolutions.idispatch.domain.Municipality;
import net.pkhsolutions.idispatch.domain.tickets.Ticket;
import net.pkhsolutions.idispatch.domain.tickets.TicketService;
import net.pkhsolutions.idispatch.domain.tickets.TicketType;
import net.pkhsolutions.idispatch.domain.tickets.TicketUrgency;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.util.Date;

/**
 * Model for showing and editing a single {@link net.pkhsolutions.idispatch.domain.tickets.Ticket}. Changes are directly propagated to the database.
 */
@Component
@Scope(value = "prototype")
public class TicketModel {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    // TODO Make the model observable and notify observers when the ticket is changed elsewhere.

    @Autowired
    TicketService ticketService;

    private Ticket ticket;

    protected Ticket getTicket() {
        if (ticket == null) {
            throw new IllegalStateException("No ticket set");
        }
        return ticket;
    }

    public void setTicket(Ticket ticket) {
        Assert.notNull(ticket, "Cannot specify a null ticket");
        this.ticket = ticket;
    }

    protected void perform(TicketOperation operation) {
        if (isEditable()) {
            Ticket.Builder builder = new Ticket.Builder(getTicket());
            operation.perform(builder);
            ticket = ticketService.updateTicket(builder.build());
        } else {
            logger.warn("Attempted to modify a closed ticket");
        }
    }

    public boolean isEditable() {
        return !getTicket().isClosed();
    }

    public Long getId() {
        return getTicket().getId();
    }

    public Date getTicketOpened() {
        return getTicket().getTicketOpened();
    }

    public Date getTicketClosed() {
        return getTicket().getTicketClosed();
    }

    public TicketUrgency getUrgency() {
        return getTicket().getUrgency();
    }

    public void setUrgency(TicketUrgency urgency) {
        perform((builder) -> builder.withUrgency(urgency));
    }

    public TicketType getTicketType() {
        return getTicket().getType();
    }

    public void setTicketType(TicketType ticketType) {
        perform((builder) -> builder.withType(ticketType));
    }

    public String getDescription() {
        return getTicket().getDescription();
    }

    public void setDescription(String description) {
        perform((builder) -> builder.withDescription(description));
    }

    public Municipality getMunicipality() {
        return getTicket().getMunicipality();
    }

    public void setMunicipality(Municipality municipality) {
        perform((builder) -> builder.withMunicipality(municipality));
    }

    public String getAddress() {
        return getTicket().getAddress();
    }

    public void setAddress(String address) {
        perform((builder) -> builder.withAddress(address));
    }

    @FunctionalInterface
    protected interface TicketOperation {
        void perform(Ticket.Builder builder);
    }
}
