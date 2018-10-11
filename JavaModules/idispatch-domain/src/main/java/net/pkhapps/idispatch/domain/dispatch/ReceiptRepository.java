package net.pkhapps.idispatch.domain.dispatch;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Repository of {@link DispatchNotificationReceipt}s.
 */
public interface ReceiptRepository extends JpaRepository<DispatchNotificationReceipt, Long> {

    List<DispatchNotificationReceipt> findByNotification(DispatchNotification notification);

    List<DispatchNotificationReceipt> findByDestination(Destination destination);

}
