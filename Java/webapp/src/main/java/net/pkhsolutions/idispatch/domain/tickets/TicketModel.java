package net.pkhsolutions.idispatch.domain.tickets;

import net.pkhsolutions.idispatch.domain.Municipality;
import net.pkhsolutions.idispatch.domain.tickets.events.TicketCreatedEvent;
import net.pkhsolutions.idispatch.domain.tickets.events.TicketUpdatedEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.vaadin.spring.events.EventBus;
import org.vaadin.spring.events.EventBusScope;
import org.vaadin.spring.events.EventScope;

import java.util.Date;

/**
 * Model for showing and editing a single {@link Ticket}. Changes are directly propagated to the database.
 */
@Service
@Scope(value = "prototype", proxyMode = ScopedProxyMode.TARGET_CLASS)
public class TicketModel {

    @Autowired
    TicketRepository ticketRepository;

    @Autowired
    @EventBusScope(EventScope.APPLICATION)
    EventBus eventBus;

    private Ticket ticket;

    /**
     * Attempts to load the ticket with the specified ID. Returns true if found, false if not.
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public boolean load(long ticketId) {
        ticket = ticketRepository.findOne(ticketId);
        return ticket != null;
    }

    /**
     * Creates a new ticket and stores it.
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void create() {
        ticket = ticketRepository.saveAndFlush(new Ticket.Builder().build());
        eventBus.publish(this, new TicketCreatedEvent(ticket));
    }

    protected Ticket getTicket() {
        if (ticket == null) {
            throw new IllegalStateException("No ticket loaded");
        }
        return ticket;
    }

    @FunctionalInterface
    protected interface TicketOperation {
        void perform(Ticket.Builder builder);
    }

    protected void perform(TicketOperation operation) {
        Ticket.Builder builder = new Ticket.Builder(getTicket());
        operation.perform(builder);
        ticket = ticketRepository.saveAndFlush(builder.build());
        // TODO Handle optimistic locking errors
        eventBus.publish(this, new TicketUpdatedEvent(ticket));
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

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void setUrgency(TicketUrgency urgency) {
        perform((builder) -> builder.withUrgency(urgency));
    }

    public TicketType getTicketType() {
        return getTicket().getTicketType();
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void setTicketType(TicketType ticketType) {
        perform((builder) -> builder.withTicketType(ticketType));
    }

    public String getDescription() {
        return getTicket().getDescription();
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void setDescription(String description) {
        perform((builder) -> builder.withDescription(description));
    }

    public Municipality getMunicipality() {
        return getTicket().getMunicipality();
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void setMunicipality(Municipality municipality) {
        perform((builder) -> builder.withMunicipality(municipality));
    }

    public String getAddress() {
        return getTicket().getAddress();
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void setAddress(String address) {
        perform((builder) -> builder.withAddress(address));
    }
}
