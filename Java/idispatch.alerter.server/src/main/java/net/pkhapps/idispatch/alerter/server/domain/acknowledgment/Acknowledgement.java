package net.pkhapps.idispatch.alerter.server.domain.acknowledgment;

import lombok.Getter;
import net.pkhapps.idispatch.alerter.server.domain.DbConstants;
import net.pkhapps.idispatch.alerter.server.domain.alert.AlertId;
import net.pkhapps.idispatch.alerter.server.domain.recipient.RecipientId;
import net.pkhapps.idispatch.base.domain.AggregateRoot;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.time.Instant;

import static java.util.Objects.requireNonNull;

/**
 * Acknowledgment that a certain alert has been received by a certain recipient.
 */
@Entity
@Table(schema = DbConstants.SCHEMA_NAME, name = "alert_ack")
@Getter
public class Acknowledgement extends AggregateRoot<AcknowledgementId> {

    @Column(name = "ack_date")
    private Instant acknowledgementDate;

    @Column(name = "alert_id")
    private AlertId alert;

    @Column(name = "recipient_id")
    private RecipientId recipient;

    protected Acknowledgement() {
        super(AcknowledgementIdConverter.INSTANCE);
    }

    public Acknowledgement(Instant acknowledgementDate, AlertId alert, RecipientId recipient) {
        this();
        setAcknowledgementDate(acknowledgementDate);
        setAlert(alert);
        setRecipient(recipient);
        registerEvent(new AlertAcknowledgedEvent(this));
    }

    private void setAcknowledgementDate(Instant acknowledgementDate) {
        this.acknowledgementDate = requireNonNull(acknowledgementDate);
    }

    private void setAlert(AlertId alert) {
        this.alert = requireNonNull(alert);
    }

    private void setRecipient(RecipientId recipient) {
        this.recipient = requireNonNull(recipient);
    }
}
