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

import java.util.function.Consumer;

/**
 * Interface representing the iDispatch Alert Server.
 */
public interface AlertServer {

    /**
     * Sends an alert to a specific set of resources. The alert server will decide which receivers will actually receive
     * the alert.
     *
     * @param command the command to send.
     */
    void sendAlert(SendAlertCommand command);

    /**
     * Acknowledges that an alert has been received by a receiver.
     *
     * @param command the command.
     */
    void acknowledgeAlert(AcknowledgeAlertCommand command);

    /**
     * Subscribes to alerts sent to the given receiver.
     *
     * @param receiverId    the ID of the receiver.
     * @param alertConsumer the callback to be notified when an alert arrives.
     * @return a {@link Subscription} object.
     */
    Subscription subscribeToAlerts(AlertReceiverId receiverId, Consumer<Alert> alertConsumer);

    /**
     * Interface defining a subscription to alerts sent to a specific receiver.
     */
    interface Subscription {
        /**
         * Unsubscribes the receiver. After this, no more alerts will be delivered.
         */
        void unsubscribe();

        /**
         * Informs the alert server that the receiver of the subscription is alive and well.
         */
        void pong();
    }
}
