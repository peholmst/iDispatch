package net.pkhsolutions.idispatch.entity;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"receiver_id", "notification_id"}))
public class DispatchNotificationReceipt extends AbstractEntity {

    @ManyToOne(optional = false)
    @JoinColumn(name = "receiver_id", nullable = false)
    private DispatchNotificationReceiver receiver;
    @ManyToOne(optional = false)
    @JoinColumn(name = "notification_id", nullable = false)
    private DispatchNotification notification;

    protected DispatchNotificationReceipt() {
    }

    public DispatchNotificationReceipt(DispatchNotificationReceiver receiver, DispatchNotification notification) {
        this.receiver = receiver;
        this.notification = notification;
    }

    public DispatchNotificationReceiver getReceiver() {
        return receiver;
    }

    public DispatchNotification getNotification() {
        return notification;
    }
}
