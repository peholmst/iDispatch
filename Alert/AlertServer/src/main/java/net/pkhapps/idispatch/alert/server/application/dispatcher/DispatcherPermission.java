package net.pkhapps.idispatch.alert.server.application.dispatcher;

import net.pkhapps.idispatch.alert.server.application.security.Permission;

/**
 * Enumeration of permissions that apply to the Dispatcher port.
 */
public enum DispatcherPermission implements Permission {
    /**
     * Permission to send out alerts.
     */
    SEND_ALERT,
    /**
     * Permission to check the status of already sent alerts.
     */
    CHECK_ALERT_STATUS
}
