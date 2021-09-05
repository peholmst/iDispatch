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
package net.pkhapps.idispatch.alert.server.application.ports.receiver;

import net.pkhapps.idispatch.alert.server.data.AlertId;
import net.pkhapps.idispatch.alert.server.data.ReceiverId;

/**
 * Port interface for alert receivers that (like the name suggests) receives alerts from the dispatchers and notify the
 * alerted resources in different ways.
 *
 * @see #registerCourier(Courier)
 */
public interface ReceiverPort {

    /**
     * Informs the system that the specified alert has been successfully delivered
     * to the specified receiver.
     *
     * @param receiverId the ID of the receiver to which the alert has been
     *                   delivered.
     * @param alertId    the ID of the alert that has been delivered.
     */
    void acknowledgeDelivery(ReceiverId receiverId, AlertId alertId);

    /**
     * Informs the system that the specified receiver is up and ready to receive
     * alerts.
     *
     * @param receiverId the ID of the receiver.
     */
    void receiverUp(ReceiverId receiverId);

    /**
     * Informs the system that the specified receiver is down and unable to receive
     * alerts.
     *
     * @param receiverId the ID of the receiver.
     */
    void receiverDown(ReceiverId receiverId);

    /**
     * Registers a new {@link Courier} with the system. A courier is used to deliver
     * alerts to a specific type of receivers (such as mobile phone apps, station
     * alert systems, e-mail, SMS).
     *
     * @param courier the courier to register.
     * @return a registration handle that can be used to unregister the courier
     * during runtime.
     */
    CourierRegistration registerCourier(Courier courier);
}
