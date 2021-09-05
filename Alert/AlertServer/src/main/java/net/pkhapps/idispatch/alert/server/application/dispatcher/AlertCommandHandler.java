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

import net.pkhapps.idispatch.alert.server.application.ports.dispatcher.AlertCommand;
import net.pkhapps.idispatch.alert.server.data.AlertId;
import net.pkhapps.idispatch.alert.server.data.AlertRepository;
import net.pkhapps.idispatch.alert.server.data.ReceiverRepository;

import static java.util.Objects.requireNonNull;

@Deprecated(forRemoval = true)
class AlertCommandHandler {

    // TODO Implement me
    private final AlertRepository alertRepository;
    private final ReceiverRepository receiverRepository;

    AlertCommandHandler(AlertRepository alertRepository, ReceiverRepository receiverRepository) {
        this.alertRepository = requireNonNull(alertRepository, "alertRepository must not be null");
        this.receiverRepository = requireNonNull(receiverRepository, "receiverRepository must not be null");
    }

    AlertId alert(AlertCommand command) {
        requireNonNull(command, "command must not be null");
        // var essence = new Alert.Essence(alertRepository.generateId());
        // var receivers =
        // receiverRepository.findByResources(command.getResourcesToAlert());

        throw new UnsupportedOperationException("Not implemented");
    }
}
