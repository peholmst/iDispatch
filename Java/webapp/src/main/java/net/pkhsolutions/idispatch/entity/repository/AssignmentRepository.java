package net.pkhsolutions.idispatch.entity.repository;

import net.pkhsolutions.idispatch.entity.Assignment;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository of {@link net.pkhsolutions.idispatch.entity.Assignment}s.
 */
public interface AssignmentRepository extends JpaRepository<Assignment, Long> {
}
