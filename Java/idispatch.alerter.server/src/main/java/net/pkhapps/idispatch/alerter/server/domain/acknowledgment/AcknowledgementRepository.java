package net.pkhapps.idispatch.alerter.server.domain.acknowledgment;

import net.pkhapps.idispatch.alerter.server.domain.alert.AlertId;
import net.pkhapps.idispatch.alerter.server.domain.recipient.RecipientId;
import net.pkhapps.idispatch.base.domain.Repository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Repository interface for {@link Acknowledgement}.
 */
public interface AcknowledgementRepository extends Repository<Acknowledgement, AcknowledgementId> {

    Page<Acknowledgement> findByAlert(AlertId alert, Pageable pageable);

    Page<Acknowledgement> findByRecipient(RecipientId recipient, Pageable pageable);

    Optional<Acknowledgement> findByRecipientAndAlert(RecipientId recipient, AlertId alert);

    boolean existsByRecipientAndAlert(RecipientId recipient, AlertId alert);
}
