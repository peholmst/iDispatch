package net.pkhsolutions.idispatch.entity.repository;

import net.pkhsolutions.idispatch.entity.ArchivedResourceStatus;
import net.pkhsolutions.idispatch.entity.Assignment;
import net.pkhsolutions.idispatch.entity.Resource;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Repository of {@link net.pkhsolutions.idispatch.entity.ArchivedResourceStatus}es.
 */
public interface ArchivedResourceStatusRepository extends JpaRepository<ArchivedResourceStatus, Long> {

    List<ArchivedResourceStatus> findByAssignment(Assignment assignment, Pageable pageable);

    List<ArchivedResourceStatus> findByAssignment(Assignment assignment);

    List<ArchivedResourceStatus> findByResource(Resource resource, Pageable pageable);

}
