package net.pkhapps.idispatch.identity.server.domain;

/**
 * Enumeration of user types.
 */
public enum UserType {
    /**
     * The user represents an individual (a human being).
     */
    INDIVIDUAL,
    /**
     * The user represents a role that is held by different individuals in turn.
     */
    ROLE,
    /**
     * The user represents a physical device.
     */
    DEVICE,
    /**
     * The user represents a system.
     */
    SYSTEM
}
