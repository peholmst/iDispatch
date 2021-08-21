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
package net.pkhapps.idispatch.alert.server.application.ports.dispatcher;

import net.pkhapps.idispatch.alert.server.application.security.Permission;

/**
 * Enumeration of permissions that apply to the Dispatcher port.
 */
public enum DispatcherPermission implements Permission {
    /**
     * Permission to send out alerts.
     */
    SEND_ALERT,
    /**
     * Permission to check the status of already sent alerts.
     */
    CHECK_ALERT_STATUS
}
