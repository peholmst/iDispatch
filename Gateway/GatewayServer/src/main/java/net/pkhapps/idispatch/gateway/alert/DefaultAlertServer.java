/*
 * iDispatch Gateway Server
 * Copyright (C) 2021 Petter Holmström
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package net.pkhapps.idispatch.gateway.alert;

import net.pkhapps.idispatch.messages.alert.Alert;
import net.pkhapps.idispatch.messages.alert.commands.AcknowledgeAlertCommand;
import net.pkhapps.idispatch.messages.alert.commands.SendAlertCommand;
import net.pkhapps.idispatch.messages.identifiers.AlertReceiverId;

import javax.enterprise.context.ApplicationScoped;
import java.util.function.Consumer;

/**
 * TODO Document me
 */
@ApplicationScoped
public class DefaultAlertServer implements AlertServer {

    // TODO Implement me

    @Override
    public void sendAlert(SendAlertCommand command) {

    }

    @Override
    public void acknowledgeAlert(AcknowledgeAlertCommand command) {

    }

    @Override
    public Subscription subscribeToAlerts(AlertReceiverId receiverId, Consumer<Alert> alertConsumer) {
        return null;
    }
}
