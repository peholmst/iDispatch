package net.pkhsolutions.idispatch.ejb.tickets;

import net.pkhsolutions.idispatch.entity.DispatchNotification;

public class DispatchNotificationCreatedEvent implements java.io.Serializable {

    private final DispatchNotification notification;

    public DispatchNotificationCreatedEvent(DispatchNotification notification) {
        this.notification = notification;
    }

    public DispatchNotification getNotification() {
        return notification;
    }
}
