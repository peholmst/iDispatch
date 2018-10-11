package net.pkhapps.idispatch.domain.dispatch;

import net.pkhapps.idispatch.domain.base.AbstractAggregateRoot;

import javax.persistence.*;
import java.time.Instant;

/**
 * TODO Document me!
 */
@Entity
@Table(name = "receipts", uniqueConstraints = @UniqueConstraint(columnNames = {"destination_id", "notification_id"}))
public class DispatchNotificationReceipt extends AbstractAggregateRoot<DispatchNotificationReceiptId> {

    @Column(name = "received_on", nullable = false)
    private Instant receivedOn;

    @ManyToOne(optional = false)
    @JoinColumn(name = "destination_id", nullable = false)
    private Destination destination;

    @ManyToOne(optional = false)
    @JoinColumn(name = "notification_id", nullable = false)
    private DispatchNotification notification;


    // TODO Implement me!
    protected DispatchNotificationReceipt() {
    }

    public DispatchNotificationReceipt(Destination destination, DispatchNotification notification) {
        this.destination = destination;
        this.notification = notification;
    }

    public Destination getDestination() {
        return destination;
    }

    public DispatchNotification getNotification() {
        return notification;
    }
}
