package net.pkhsolutions.idispatch.entity.repository;

import net.pkhsolutions.idispatch.entity.DispatchNotification;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DispatchNotificationRepository extends JpaRepository<DispatchNotification, Long> {
}
