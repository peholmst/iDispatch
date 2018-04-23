package net.pkhsolutions.idispatch.entity.repository;

import net.pkhsolutions.idispatch.entity.Assignment;
import net.pkhsolutions.idispatch.entity.Resource;
import net.pkhsolutions.idispatch.entity.ResourceStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * Repository of {@link net.pkhsolutions.idispatch.entity.ResourceStatus}es.
 */
public interface ResourceStatusRepository extends JpaRepository<ResourceStatus, Long> {

    ResourceStatus findByResource(Resource resource);

    List<ResourceStatus> findByAssignment(Assignment assignment);

    @Query("select rs from ResourceStatus rs where rs.resource.active = TRUE and rs.available = TRUE order by rs.resource.callSign")
    List<ResourceStatus> findByActiveTrueAndAvailableTrue();

    List<ResourceStatus> findByAssignmentAndAssignedTrue(Assignment assignment);

    @Query("select rs from ResourceStatus rs where rs.resource.active = TRUE order by rs.resource.callSign")
    List<ResourceStatus> findByActiveTrue();
}
