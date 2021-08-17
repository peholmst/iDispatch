package net.pkhapps.idispatch.alert.server.application.security;

/**
 * Interface defining a security manager that is used by the application layer
 * to enforce security.
 */
public interface SecurityManager {

    /**
     * Checks if the current user has a specific permission and throws an exception
     * if not.
     * 
     * @param permission the permission that the current user should have, must not
     *                   be {@code null}.
     * @throws AccessDeniedException if the current user does not have the given
     *                               permission.
     */
    void checkPermission(Permission permission);
}
