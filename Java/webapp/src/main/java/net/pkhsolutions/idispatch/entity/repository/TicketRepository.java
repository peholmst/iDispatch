package net.pkhsolutions.idispatch.entity.repository;

import net.pkhsolutions.idispatch.entity.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository of {@link net.pkhsolutions.idispatch.entity.Ticket}s.
 */
public interface TicketRepository extends JpaRepository<Ticket, Long> {
}
