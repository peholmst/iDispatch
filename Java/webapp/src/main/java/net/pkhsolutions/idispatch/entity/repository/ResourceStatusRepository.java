package net.pkhsolutions.idispatch.entity.repository;

import net.pkhsolutions.idispatch.entity.Assignment;
import net.pkhsolutions.idispatch.entity.Resource;
import net.pkhsolutions.idispatch.entity.ResourceState;
import net.pkhsolutions.idispatch.entity.ResourceStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Collection;
import java.util.List;

/**
 * Repository of {@link net.pkhsolutions.idispatch.entity.ResourceStatus}es.
 */
public interface ResourceStatusRepository extends JpaRepository<ResourceStatus, Long> {

    ResourceStatus findByResource(Resource resource);

    List<ResourceStatus> findByLastAssignment(Assignment assignment);

    @Query("SELECT rs FROM ResourceStatus rs WHERE rs.resource.active = TRUE AND rs.state IN ?1")
    List<ResourceStatus> findActiveResourcesInStates(Collection<ResourceState> states);

    List<ResourceStatus> findByActiveTrueAndAvailableTrue();

    List<ResourceStatus> findByLastAssignmentAndAssignedTrue(Assignment assignment);
}
