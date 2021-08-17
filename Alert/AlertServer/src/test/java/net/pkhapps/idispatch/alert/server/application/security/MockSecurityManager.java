// iDispatch Alert Server
// Copyright (C) 2021 Petter Holmström
//
// This program is free software: you can redistribute it and/or modify
// it under the terms of the GNU Affero General Public License as
// published by the Free Software Foundation, either version 3 of the
// License, or (at your option) any later version.
//
// This program is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
// GNU Affero General Public License for more details.
//
// You should have received a copy of the GNU Affero General Public License
// along with this program.  If not, see <https://www.gnu.org/licenses/>.
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
