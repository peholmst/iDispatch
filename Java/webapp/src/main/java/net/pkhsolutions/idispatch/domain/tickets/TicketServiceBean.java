package net.pkhsolutions.idispatch.domain.tickets;

import net.pkhsolutions.idispatch.domain.tickets.events.TicketCreatedEvent;
import net.pkhsolutions.idispatch.domain.tickets.events.TicketUpdatedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.vaadin.spring.events.EventBus;
import org.vaadin.spring.events.EventBusScope;
import org.vaadin.spring.events.EventScope;

import java.util.Optional;

/**
 * Implementation of {@link net.pkhsolutions.idispatch.domain.tickets.TicketService}.
 */
@Service
@Transactional(propagation = Propagation.REQUIRES_NEW)
public class TicketServiceBean implements TicketService {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    TicketRepository ticketRepository;

    @Autowired
    @EventBusScope(EventScope.APPLICATION)
    EventBus eventBus;

    @Override
    public Optional<Ticket> loadTicket(Long id) {
        if (id == null) {
            return Optional.empty();
        }
        final Ticket ticket = ticketRepository.findOne(id);
        return Optional.ofNullable(ticket);
    }

    @Override
    public Long createTicket() {
        logger.info("Creating new ticket");
        final Ticket createdTicket = ticketRepository.saveAndFlush(new Ticket.Builder().build());
        eventBus.publish(this, new TicketCreatedEvent(createdTicket));
        logger.info("Created new ticket with ID {}", createdTicket.getId());
        return createdTicket.getId();
    }

    @Override
    public Ticket updateTicket(Ticket ticket) {
        logger.info("Updating ticket with ID {}", ticket.getId());
        final Ticket updatedTicket = ticketRepository.saveAndFlush(ticket);
        eventBus.publish(this, new TicketUpdatedEvent(updatedTicket));
        return updatedTicket;
    }
}
