package net.pkhsolutions.idispatch.domain.tickets;

import net.pkhsolutions.idispatch.domain.tickets.events.TicketClosedEvent;
import net.pkhsolutions.idispatch.domain.tickets.events.TicketCreatedEvent;
import net.pkhsolutions.idispatch.domain.tickets.events.TicketUpdatedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.support.TransactionTemplate;

import javax.annotation.PostConstruct;
import java.util.Optional;

/**
 * Implementation of {@link net.pkhsolutions.idispatch.domain.tickets.TicketService}.
 */
@Service
public class TicketServiceBean implements TicketService {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    TicketRepository ticketRepository;
    @Autowired
    ApplicationEventPublisher eventPublisher;
    @Autowired
    PlatformTransactionManager transactionManager;

    private TransactionTemplate transactionTemplate;

    @PostConstruct
    void init() {
        transactionTemplate = new TransactionTemplate(transactionManager);
        transactionTemplate.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
        transactionTemplate.setName("TicketSavingTX");
    }

    @Override
    public Optional<Ticket> retrieveTicket(Long id) {
        logger.debug("Looking up ticket with ID {}", id);
        if (id == null) {
            return Optional.empty();
        }
        final Ticket ticket = ticketRepository.findOne(id);
        return Optional.ofNullable(ticket);
    }

    @Override
    public Long createTicket() {
        logger.debug("Creating new ticket");
        final Ticket createdTicket = transactionTemplate.execute(status -> ticketRepository.saveAndFlush(new Ticket()));
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
        final Ticket updatedTicket = transactionTemplate.execute(status -> ticketRepository.saveAndFlush(ticket));
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
        final Ticket closedTicket = transactionTemplate.execute(status -> ticketRepository.saveAndFlush(ticket));
        eventPublisher.publishEvent(new TicketClosedEvent(this, closedTicket));
    }
}
