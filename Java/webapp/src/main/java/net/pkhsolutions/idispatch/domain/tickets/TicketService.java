package net.pkhsolutions.idispatch.domain.tickets;

import java.util.Optional;

/**
 * Service interface for ticket operations.
 */
public interface TicketService {

    /**
     * Attempts to load the ticket with the specified ID.
     */
    Optional<Ticket> loadTicket(Long id);

    /**
     * Creates a new ticket, stores it, fires a {@link net.pkhsolutions.idispatch.domain.tickets.events.TicketCreatedEvent}
     * and returns the ticket ID.
     */
    Long createTicket();

    /**
     * Saves the existing ticket, fires a {@link net.pkhsolutions.idispatch.domain.tickets.events.TicketUpdatedEvent}
     * and returns the updated ticket instance.
     */
    Ticket updateTicket(Ticket ticket);
}
