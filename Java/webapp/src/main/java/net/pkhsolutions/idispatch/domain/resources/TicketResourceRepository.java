package net.pkhsolutions.idispatch.domain.resources;

import net.pkhsolutions.idispatch.domain.tickets.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Repository of {@link net.pkhsolutions.idispatch.domain.resources.TicketResource}s.
 */
interface TicketResourceRepository extends JpaRepository<TicketResource, Long> {

    /**
     * Finds and returns all ticket resources associated with the specific ticket.
     */
    List<TicketResource> findByTicket(Ticket ticket);

    /**
     * Finds and returns all ticket resources associated with the specific ticket and resource.
     */
    List<TicketResource> findByTicketAndResource(Ticket ticket, Resource resource);
}
