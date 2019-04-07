package net.pkhapps.idispatch.alerter.server.domain.alert;

/**
 * Enumeration of alert priorities. This has nothing to do with assignment priorities but how receivers of the alert
 * are notified. For example, a high priority alert may have a loud and long alert tone whereas a low priority alert
 * may have discrete notification tone.
 */
public enum AlertPriority {
    LOW,
    NORMAL,
    HIGH
}
