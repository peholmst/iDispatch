package net.pkhapps.idispatch.domain.repository;

import net.pkhapps.idispatch.domain.ArchivedResourceStatus;
import net.pkhapps.idispatch.domain.assignment.Assignment;
import net.pkhapps.idispatch.domain.resource.Resource;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Repository of {@link ArchivedResourceStatus}es.
 */
public interface ArchivedResourceStatusRepository extends JpaRepository<ArchivedResourceStatus, Long> {

    List<ArchivedResourceStatus> findByAssignment(Assignment assignment, Pageable pageable);

    List<ArchivedResourceStatus> findByAssignment(Assignment assignment);

    List<ArchivedResourceStatus> findByResource(Resource resource, Pageable pageable);

}
