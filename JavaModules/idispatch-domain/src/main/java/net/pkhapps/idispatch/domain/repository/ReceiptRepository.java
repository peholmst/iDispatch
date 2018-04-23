package net.pkhapps.idispatch.domain.repository;

import net.pkhapps.idispatch.domain.Destination;
import net.pkhapps.idispatch.domain.DispatchNotification;
import net.pkhapps.idispatch.domain.Receipt;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Repository of {@link Receipt}s.
 */
public interface ReceiptRepository extends JpaRepository<Receipt, Long> {

    List<Receipt> findByNotification(DispatchNotification notification);

    List<Receipt> findByDestination(Destination destination);

}
