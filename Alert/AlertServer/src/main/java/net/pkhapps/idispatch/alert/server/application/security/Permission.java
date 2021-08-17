package net.pkhapps.idispatch.alert.server.application.security;

/**
 * Interface specifying a permission that the current user should have in order
 * to be able to perform some operation. Typically {@code enum}s would implement
 * this interface, but this is not a requirement.
 */
public interface Permission {
    /**
     * Gets the name of the permission. This, in combination with the class, must be
     * enough to uniquely identify a specific permission.
     * 
     * @return the name of the permission.
     */
    String name();
}
