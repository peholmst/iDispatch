package net.pkhapps.idispatch.alerter.server.domain.recipient;

/**
 * The recipient priority is used to determine in which order different recipients are alerted. The recipients
 * with the highest priority are alerted first. This may be of significance especially when a lot of recipients are
 * being alerted at the same time.
 */
public enum RecipientPriority {
    LOW,
    NORMAL,
    HIGH
}
