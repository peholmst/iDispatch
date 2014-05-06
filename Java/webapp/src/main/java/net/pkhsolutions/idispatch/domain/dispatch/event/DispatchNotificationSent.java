package net.pkhsolutions.idispatch.domain.dispatch.event;

import net.pkhsolutions.idispatch.domain.dispatch.DispatchNotification;
import org.springframework.context.ApplicationEvent;

public class DispatchNotificationSent extends ApplicationEvent {

    private final DispatchNotification dispatchNotification;

    public DispatchNotificationSent(Object source, DispatchNotification dispatchNotification) {
        super(source);
        this.dispatchNotification = dispatchNotification;
    }

    public DispatchNotification getDispatchNotification() {
        return dispatchNotification;
    }
}
