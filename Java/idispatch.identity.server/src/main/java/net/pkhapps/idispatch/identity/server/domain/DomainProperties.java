package net.pkhapps.idispatch.identity.server.domain;

import java.time.Duration;

/**
 * Interface defining properties that can be configured externally.
 */
public interface DomainProperties {

    /**
     * The duration a user account remains unavailable after it has been locked.
     */
    Duration getUserAccountLockDuration();
}
