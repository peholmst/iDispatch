package net.pkhsolutions.idispatch.control;

import net.pkhsolutions.idispatch.entity.DispatchNotification;

import java.util.Collection;

public interface RunboardDispatcher {

    Collection<DispatchNotification> getDispatchNotifications(String runboardKey);

    void acknowledgeDispatchNotification(String runboardKey, Long notificationId);

    void cleanUp();
}
