package net.pkhsolutions.idispatch.control;

import net.pkhsolutions.idispatch.boundary.events.DispatchNotificationSent;
import net.pkhsolutions.idispatch.entity.DispatchNotification;
import org.springframework.context.ApplicationListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Collections;

@Component
class RunboardDispatcherBean implements ApplicationListener<DispatchNotificationSent>, RunboardDispatcher {

    @Override
    @Async
    public void onApplicationEvent(DispatchNotificationSent event) {

    }

    @Override
    public Collection<DispatchNotification> dequeDispatchNotifications(String runboardKey) {
        return Collections.emptyList();
    }
}
