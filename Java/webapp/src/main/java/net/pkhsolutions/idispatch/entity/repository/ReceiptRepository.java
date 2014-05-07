package net.pkhsolutions.idispatch.entity.repository;

import net.pkhsolutions.idispatch.entity.Receipt;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository of {@link net.pkhsolutions.idispatch.entity.Receipt}s.
 */
public interface ReceiptRepository extends JpaRepository<Receipt, Long> {
}
