package net.pkhsolutions.idispatch.domain.tickets;

import net.pkhsolutions.idispatch.domain.tickets.events.TicketClosedEvent;
import net.pkhsolutions.idispatch.domain.tickets.events.TicketCreatedEvent;
import net.pkhsolutions.idispatch.domain.tickets.events.TicketUpdatedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

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
    ApplicationEventPublisher eventPublisher;

    @Override
    public Optional<Ticket> retrieveTicket(Long id) {
        if (id == null) {
            return Optional.empty();
        }
        final Ticket ticket = ticketRepository.findOne(id);
        return Optional.ofNullable(ticket);
    }

    @Override
    public Long createTicket() {
        logger.debug("Creating new ticket");
        final Ticket createdTicket = ticketRepository.saveAndFlush(new Ticket());
        eventPublisher.publishEvent(new TicketCreatedEvent(this, createdTicket));
        logger.debug("Created new ticket with ID {}", createdTicket.getId());
        return createdTicket.getId();
    }

    @Override
    public Ticket updateTicket(Ticket ticket) {
        logger.debug("Updating ticket {}", ticket);
        if (ticket.isClosed()) {
            logger.debug("Ticket {} is closed, cannot update", ticket);
            return ticket;
        }
        final Ticket updatedTicket = ticketRepository.saveAndFlush(ticket);
        eventPublisher.publishEvent(new TicketUpdatedEvent(this, updatedTicket));
        return updatedTicket;
    }

    @Override
    public void closeTicket(Ticket ticket) {
        logger.debug("Closing ticket {}", ticket);
        if (ticket.isClosed()) {
            logger.debug("Ticket {} is already closed, ignoring", ticket);
            return;
        }
        ticket.close();
        final Ticket closedTicket = ticketRepository.saveAndFlush(ticket);
        eventPublisher.publishEvent(new TicketClosedEvent(this, closedTicket));
    }
}
