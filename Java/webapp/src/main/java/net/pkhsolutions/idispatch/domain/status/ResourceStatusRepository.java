package net.pkhsolutions.idispatch.domain.status;

import net.pkhsolutions.idispatch.domain.resources.Resource;
import net.pkhsolutions.idispatch.domain.tickets.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Collection;
import java.util.List;

/**
 * Repository of {@link ResourceStatus}es.
 */
public interface ResourceStatusRepository extends JpaRepository<ResourceStatus, Long> {

    ResourceStatus findByResource(Resource resource);

    List<ResourceStatus> findByTicket(Ticket ticket);

    @Query("SELECT rs FROM ResourceStatus rs WHERE rs.resource.active = TRUE AND rs.state IN ?1")
    List<ResourceStatus> findActiveResourcesInStates(Collection<ResourceState> states);

    List<ResourceStatus> findByAvailableTrue();

    List<ResourceStatus> findByTicketAndDetachedFalse(Ticket ticket);
}
