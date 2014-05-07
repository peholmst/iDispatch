package net.pkhsolutions.idispatch.entity.repository;

import net.pkhsolutions.idispatch.entity.TicketType;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository of {@link net.pkhsolutions.idispatch.entity.TicketType}s.
 */
public interface TicketTypeRepository extends JpaRepository<TicketType, Long> {
}
