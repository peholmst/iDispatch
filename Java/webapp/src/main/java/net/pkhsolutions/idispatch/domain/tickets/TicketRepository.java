package net.pkhsolutions.idispatch.domain.tickets;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository of {@link net.pkhsolutions.idispatch.domain.tickets.Ticket}s.
 */
public interface TicketRepository extends JpaRepository<Ticket, Long> {
}
