package net.pkhsolutions.idispatch.entity.repository;

import net.pkhsolutions.idispatch.entity.Assignment;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Repository of {@link net.pkhsolutions.idispatch.entity.Assignment}s.
 */
public interface AssignmentRepository extends JpaRepository<Assignment, Long> {

    List<Assignment> findByClosedIsNull();

    List<Assignment> findByClosedIsNotNull();

    List<Assignment> findByClosedIsNotNull(Pageable pageable);
}
