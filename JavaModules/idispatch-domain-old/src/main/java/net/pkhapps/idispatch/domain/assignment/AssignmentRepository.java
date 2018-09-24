package net.pkhapps.idispatch.domain.assignment;

import net.pkhapps.idispatch.domain.base.Repository;
import net.pkhapps.idispatch.domain.resource.ResourceId;
import org.springframework.data.jpa.repository.Query;
import org.springframework.lang.NonNull;

import java.util.List;

/**
 * Repository of {@link Assignment}s.
 */
public interface AssignmentRepository extends Repository<Assignment, AssignmentId> {

    @NonNull
    @Query("select a from Assignment a join a.assignmentResources ar where a.closed is null and ar.resource = :resource")
    List<Assignment> findOpenAssignmentsForResource(@NonNull ResourceId resource);
}
