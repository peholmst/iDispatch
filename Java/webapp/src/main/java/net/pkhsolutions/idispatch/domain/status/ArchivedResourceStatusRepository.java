package net.pkhsolutions.idispatch.domain.status;

import net.pkhsolutions.idispatch.domain.resources.Resource;
import net.pkhsolutions.idispatch.domain.tickets.Ticket;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Repository of {@link ArchivedResourceStatus}es.
 */
public interface ArchivedResourceStatusRepository extends JpaRepository<ArchivedResourceStatus, Long> {

    List<ArchivedResourceStatus> findByTicket(Ticket ticket, Pageable pageable);

    List<ArchivedResourceStatus> findByResource(Resource resource, Pageable pageable);

}
