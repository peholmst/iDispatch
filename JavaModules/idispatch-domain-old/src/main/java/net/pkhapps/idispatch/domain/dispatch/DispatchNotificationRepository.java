package net.pkhapps.idispatch.domain.dispatch;

import net.pkhapps.idispatch.domain.assignment.Assignment;
import net.pkhapps.idispatch.domain.dispatch.DispatchNotification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Repository of {@link DispatchNotification}s.
 */
public interface DispatchNotificationRepository extends JpaRepository<DispatchNotification, Long> {

    List<DispatchNotification> findByAssignment(Assignment assignment);
}