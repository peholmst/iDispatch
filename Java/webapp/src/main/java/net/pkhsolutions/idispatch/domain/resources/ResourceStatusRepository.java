package net.pkhsolutions.idispatch.domain.resources;

import net.pkhsolutions.idispatch.domain.tickets.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * Repository of {@link net.pkhsolutions.idispatch.domain.resources.ResourceStatus}es.
 */
public interface ResourceStatusRepository extends JpaRepository<ResourceStatus, Long> {

    ResourceStatus findByResource(Resource resource);

    List<ResourceStatus> findByTicket(Ticket ticket);

    @Query("SELECT rs FROM ResourceStatus rs WHERE rs.resource.active = TRUE AND rs.state = AT_STATION OR rs.state = AVAILABLE")
    List<ResourceStatus> findActiveAssignableResources();

}
