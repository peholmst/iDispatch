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

import net.pkhapps.idispatch.alert.server.data.AlertId;

import java.util.Optional;

/**
 * Port interface for dispatchers that dispatch (alert) resources to different
 * incidents.
 */
public interface DispatcherPort {

    /**
     * Alerts resources to a specific incident in accordance with the given
     * {@linkplain AlertCommand command}.
     *
     * @param command the command directing what to alert and where.
     * @return an {@linkplain AlertId identifier} that can be used to
     * {@linkplain #getStatus(AlertId) check the status} of the alert.
     */
    AlertId alert(AlertCommand command);

    /**
     * Gets the status of the given alert.
     *
     * @param alertId the ID of the alert whose status should be checked.
     * @return an {@code Optional} containing the status of the alert if found.
     */
    Optional<AlertStatus> getStatus(AlertId alertId);
}
