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

import io.smallrye.jwt.build.Jwt;

import java.util.Set;

/**
 * TODO Document me
 */
public final class AlertTokens {

    private AlertTokens() {
    }

    public static String getAlertReceiverAccessToken(long alertReceiverId) {
        return Jwt.preferredUserName("alert-receiver-" + alertReceiverId)
                .groups(Set.of(AlertRoles.ROLE_RECEIVER, AlertRoles.receiverSpecificRole(alertReceiverId)))
                .issuer("https://server.example.com")
                .audience("https://service.example.com")
                .sign();
    }

    public static String getDispatcherAccessToken(String username) {
        return Jwt.preferredUserName(username)
                .groups(AlertRoles.ROLE_DISPATCHER)
                .issuer("https://server.example.com")
                .audience("https://service.example.com")
                .sign();
    }
}
