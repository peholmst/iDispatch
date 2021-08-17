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
package net.pkhapps.idispatch.alert.server.application.dispatcher;

import static java.util.Objects.requireNonNull;

import java.util.Optional;

import net.pkhapps.idispatch.alert.server.application.security.SecurityManager;
import net.pkhapps.idispatch.alert.server.domain.infra.UnitOfWorkManager;
import net.pkhapps.idispatch.alert.server.domain.model.AlertId;

/**
 * Default implementation of {@link DispatcherPort} that enforces security and
 * manages units of work but otherwise delegates to {@link AlertCommandHandler}
 * and {@link AlertStatusHandler}.
 */
class DispatcherPortImpl implements DispatcherPort {

    private final UnitOfWorkManager unitOfWorkManager;
    private final SecurityManager securityManager;
    private final AlertCommandHandler alertCommandHandler;
    private final AlertStatusHandler alertStatusHandler;

    DispatcherPortImpl(UnitOfWorkManager unitOfWorkManager, SecurityManager securityManager,
            AlertCommandHandler alertCommandHandler, AlertStatusHandler alertStatusHandler) {
        this.unitOfWorkManager = requireNonNull(unitOfWorkManager, "unitOfWorkManager must not be null");
        this.securityManager = requireNonNull(securityManager, "securityManager must not be null");
        this.alertCommandHandler = requireNonNull(alertCommandHandler, "alertCommandHandler must not be null");
        this.alertStatusHandler = requireNonNull(alertStatusHandler, "alertStatusHandler must not be null");
    }

    @Override
    public AlertId alert(AlertCommand command) {
        securityManager.checkPermission(DispatcherPermission.SEND_ALERT);
        return unitOfWorkManager.performUnitOfWork(() -> alertCommandHandler.alert(command));
    }

    @Override
    public Optional<AlertStatus> getStatus(AlertId alertId) {
        securityManager.checkPermission(DispatcherPermission.CHECK_ALERT_STATUS);
        return unitOfWorkManager.performUnitOfWork(() -> alertStatusHandler.getStatus(alertId));
    }
}
