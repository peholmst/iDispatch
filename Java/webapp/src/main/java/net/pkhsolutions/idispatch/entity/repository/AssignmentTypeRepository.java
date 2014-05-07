package net.pkhsolutions.idispatch.entity.repository;

import net.pkhsolutions.idispatch.entity.AssignmentType;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository of {@link net.pkhsolutions.idispatch.entity.AssignmentType}s.
 */
public interface AssignmentTypeRepository extends JpaRepository<AssignmentType, Long> {
}
