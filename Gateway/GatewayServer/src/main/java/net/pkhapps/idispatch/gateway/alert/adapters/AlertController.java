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

import io.quarkus.security.identity.SecurityIdentity;
import net.pkhapps.idispatch.gateway.alert.AlertServer;
import net.pkhapps.idispatch.gateway.protobuf.ContentTypes;
import net.pkhapps.idispatch.messages.alert.commands.AcknowledgeAlertCommand;
import net.pkhapps.idispatch.messages.alert.commands.SendAlertCommand;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.security.DenyAll;
import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;

/**
 * REST controller for sending alerts (by Dispatchers) and acknowledging alerts (by Alert Receivers). The controller
 * perform authentication and authorization, then delegates to an {@link AlertServer}. None of the commands return any
 * results.
 */
@Path("/alert")
@Consumes(ContentTypes.PROTOBUF)
@Produces(ContentTypes.PROTOBUF)
@DenyAll
public class AlertController {

    private static final Logger log = LoggerFactory.getLogger(AlertController.class);
    private final AlertServer server;
    private final SecurityIdentity securityIdentity;

    @Inject
    public AlertController(AlertServer server, SecurityIdentity securityIdentity) {
        this.server = server;
        this.securityIdentity = securityIdentity;
    }

    @Path("/send")
    @POST
    @RolesAllowed(AlertRoles.ROLE_DISPATCHER)
    public void sendAlert(SendAlertCommand command) {
        server.sendAlert(command);
    }

    @Path("/ack")
    @POST
    @RolesAllowed(AlertRoles.ROLE_RECEIVER)
    public void acknowledgeAlert(AcknowledgeAlertCommand command) {
        if (!command.hasReceiver()) {
            log.warn("AcknowledgeAlertCommand ({}) did not have a receiver", command);
            throw new WebApplicationException(Response.Status.BAD_REQUEST);
        }
        if (!securityIdentity.hasRole(AlertRoles.receiverSpecificRole(command.getReceiver()))) {
            log.warn("AcknowledgeAlertCommand ({}) was sent by user ({}) without correct access", command,
                    securityIdentity.getPrincipal());
            throw new WebApplicationException(Response.Status.FORBIDDEN);
        }
        server.acknowledgeAlert(command);
    }
}
