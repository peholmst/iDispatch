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

package net.pkhapps.idispatch.gateway.alert.adapters;

import net.pkhapps.idispatch.messages.identifiers.AlertReceiverId;

/**
 * Roles required to send and receive alerts.
 */
public final class AlertRoles {

    /**
     * The role that all Dispatcher clients must hold in order to be allowed to send out alerts.
     */
    public static final String ROLE_DISPATCHER = "alert-dispatcher";
    /**
     * The role that all Alert Receiver clients must hold in order to be allowed to receive alerts. In addition, they
     * need a {@link #receiverSpecificRole(Long) receiver specific role}.
     */
    public static final String ROLE_RECEIVER = "alert-receiver";

    private AlertRoles() {
    }

    /**
     * Creates a role that is specific to the receiver with the given ID. This is needed to make sure that messages
     * concerning a particular receiver are being sent to and from a client that corresponds to the correct receiver.
     *
     * @param receiverId the receiver ID.
     * @return the role.
     */
    public static String receiverSpecificRole(Long receiverId) {
        return ROLE_RECEIVER + receiverId;
    }

    /**
     * Creates a role that is specific to the receiver with the given ID. This is needed to make sure that messages
     * concerning a particular receiver are being sent to and from a client that corresponds to the correct receiver.
     *
     * @param receiverId the receiver ID.
     * @return the role.
     */
    public static String receiverSpecificRole(AlertReceiverId receiverId) {
        return receiverSpecificRole(receiverId.getId());
    }
}
