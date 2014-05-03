package net.pkhsolutions.idispatch.domain.tickets;

import java.util.Optional;

/**
 * Service interface for ticket operations.
 */
public interface TicketService {

    /**
     * Attempts to retrieve the ticket with the specified ID. If the ID is {@code null}, or the ticket
     * does not exist, an empty {@code Optional} is returned.
     */
    Optional<Ticket> retrieveTicket(Long id);

    /**
     * Creates a new ticket, stores it, fires a {@link net.pkhsolutions.idispatch.domain.tickets.events.TicketCreatedEvent}
     * and returns the ticket ID.
     */
    Long createTicket();

    /**
     * Saves the existing ticket, fires a {@link net.pkhsolutions.idispatch.domain.tickets.events.TicketUpdatedEvent}
     * and returns the updated ticket instance. If the ticket is closed, the passed in ticket instance
     * will be returned immediately and no actions will be taken.
     */
    Ticket updateTicket(Ticket ticket);

    /**
     * Closes the ticket and fires a {@link net.pkhsolutions.idispatch.domain.tickets.events.TicketClosedEvent}. If
     * the ticket is already closed, nothing will happen.
     */
    void closeTicket(Ticket ticket);
}
