package net.pkhsolutions.idispatch.entity.repository;

import net.pkhsolutions.idispatch.entity.Assignment;
import net.pkhsolutions.idispatch.entity.DispatchNotification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Repository of {@link net.pkhsolutions.idispatch.entity.DispatchNotification}s.
 */
public interface DispatchNotificationRepository extends JpaRepository<DispatchNotification, Long> {

    List<DispatchNotification> findByAssignment(Assignment assignment);
}
