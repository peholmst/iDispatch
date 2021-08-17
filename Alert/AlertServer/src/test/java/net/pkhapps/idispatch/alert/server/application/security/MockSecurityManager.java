package net.pkhapps.idispatch.alert.server.application.security;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Set;

/**
 * A mock implementation of {@link SecurityManager} designed to be used in unit
 * tests.
 */
public class MockSecurityManager implements SecurityManager {

    private Set<Permission> currentPermissions = Collections.emptySet();

    /**
     * Sets the permissions that the "current user" will have.
     * 
     * @param permissions an array of permissions, may be empty.
     */
    public void setPermissionsOfCurrentUser(Permission... permissions) {
        setPermissionsOfCurrentUser(Arrays.asList(permissions));
    }

    /**
     * Sets the permissions that the "current user" will have.
     * 
     * @param permissions a collection of permissions, may be empty but never
     *                    {@code null}.
     */
    public void setPermissionsOfCurrentUser(Collection<Permission> permissions) {
        currentPermissions = Set.copyOf(permissions);
    }

    @Override
    public void checkPermission(Permission permission) {
        if (!currentPermissions.contains(permission)) {
            throw new AccessDeniedException();
        }
    }
}
