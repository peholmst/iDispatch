package net.pkhsolutions.idispatch.entity.repository;

import net.pkhsolutions.idispatch.entity.ArchivedResourceStatus;
import net.pkhsolutions.idispatch.entity.Resource;
import net.pkhsolutions.idispatch.entity.Ticket;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Repository of {@link net.pkhsolutions.idispatch.entity.ArchivedResourceStatus}es.
 */
public interface ArchivedResourceStatusRepository extends JpaRepository<ArchivedResourceStatus, Long> {

    List<ArchivedResourceStatus> findByTicket(Ticket ticket, Pageable pageable);

    List<ArchivedResourceStatus> findByResource(Resource resource, Pageable pageable);

}
