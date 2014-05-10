package net.pkhsolutions.idispatch.control;

import net.pkhsolutions.idispatch.entity.DispatchNotification;

import java.util.Collection;

public interface RunboardDispatcher {

    Collection<DispatchNotification> dequeDispatchNotifications(String runboardKey);

}
