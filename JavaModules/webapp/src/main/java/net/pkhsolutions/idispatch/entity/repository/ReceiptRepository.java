package net.pkhsolutions.idispatch.entity.repository;

import net.pkhsolutions.idispatch.entity.Destination;
import net.pkhsolutions.idispatch.entity.DispatchNotification;
import net.pkhsolutions.idispatch.entity.Receipt;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Repository of {@link net.pkhsolutions.idispatch.entity.Receipt}s.
 */
public interface ReceiptRepository extends JpaRepository<Receipt, Long> {

    List<Receipt> findByNotification(DispatchNotification notification);

    List<Receipt> findByDestination(Destination destination);

}
