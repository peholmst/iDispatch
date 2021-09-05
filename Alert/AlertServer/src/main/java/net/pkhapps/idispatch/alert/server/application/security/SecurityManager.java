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
