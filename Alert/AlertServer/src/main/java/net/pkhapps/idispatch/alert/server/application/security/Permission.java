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
