package net.pkhsolutions.idispatch.domain.tickets;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository of {@link net.pkhsolutions.idispatch.domain.tickets.TicketType}s.
 */
public interface TicketTypeRepository extends JpaRepository<TicketType, Long> {
}
