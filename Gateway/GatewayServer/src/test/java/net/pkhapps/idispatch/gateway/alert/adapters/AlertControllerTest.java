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

import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.oidc.server.OidcWiremockTestResource;
import net.pkhapps.idispatch.gateway.alert.MockAlertServer;
import net.pkhapps.idispatch.gateway.protobuf.ContentTypes;
import net.pkhapps.idispatch.messages.alert.commands.AcknowledgeAlertCommand;
import net.pkhapps.idispatch.messages.alert.commands.SendAlertCommand;
import net.pkhapps.idispatch.messages.identifiers.AlertReceiverId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@QuarkusTest
@QuarkusTestResource(OidcWiremockTestResource.class)
public class AlertControllerTest {

    @Inject
    MockAlertServer mockAlertServer;

    @BeforeEach
    public void setUp() {
        mockAlertServer.reset();
    }

    @Test
    public void testAckEndpoint_authenticatedAsCorrectReceiver() {
        var command = AcknowledgeAlertCommand.newBuilder()
                .setReceiver(AlertReceiverId.newBuilder().setId(123L).build())
                .build();
        given()
                .auth().oauth2(AlertTokens.getAlertReceiverAccessToken(123L))
                .when()
                .body(command.toByteArray())
                .contentType(ContentTypes.PROTOBUF)
                .post("/alert/ack")
                .then().statusCode(204);

        assertEquals(command, mockAlertServer.lastAcknowledgedAlert());
    }

    @Test
    public void testAckEndpoint_authenticatedAsIncorrectReceiver() {
        var command = AcknowledgeAlertCommand.newBuilder()
                .setReceiver(AlertReceiverId.newBuilder().setId(123L).build())
                .build();
        given()
                .auth().oauth2(AlertTokens.getAlertReceiverAccessToken(4546L))
                .when()
                .body(command.toByteArray())
                .contentType(ContentTypes.PROTOBUF)
                .post("/alert/ack")
                .then().statusCode(403);

        assertTrue(mockAlertServer.acknowledgedAlerts().isEmpty());
    }

    @Test
    public void testAckEndpoint_authenticatedAsDispatcher() {
        var command = AcknowledgeAlertCommand.newBuilder()
                .setReceiver(AlertReceiverId.newBuilder().setId(123L).build())
                .build();
        given()
                .auth().oauth2(AlertTokens.getDispatcherAccessToken("dispatch-server"))
                .when()
                .body(command.toByteArray())
                .contentType(ContentTypes.PROTOBUF)
                .post("/alert/ack")
                .then().statusCode(403);

        assertTrue(mockAlertServer.acknowledgedAlerts().isEmpty());
    }

    @Test
    public void testAckEndpoint_unauthenticated() {
        var command = AcknowledgeAlertCommand.newBuilder().build();
        given()
                .when()
                .body(command.toByteArray())
                .contentType(ContentTypes.PROTOBUF)
                .post("/alert/ack")
                .then().statusCode(401);

        assertTrue(mockAlertServer.acknowledgedAlerts().isEmpty());
    }

    @Test
    public void testAckEndpoint_missingReceiverId() {
        var command = AcknowledgeAlertCommand.newBuilder().build();
        given()
                .auth().oauth2(AlertTokens.getAlertReceiverAccessToken(4546L))
                .when()
                .body(command.toByteArray())
                .contentType(ContentTypes.PROTOBUF)
                .post("/alert/ack")
                .then().statusCode(400);

        assertTrue(mockAlertServer.acknowledgedAlerts().isEmpty());
    }

    @Test
    public void testSendEndpoint_authenticatedAsDispatcher() {
        var command = SendAlertCommand.newBuilder().build();
        given()
                .auth().oauth2(AlertTokens.getDispatcherAccessToken("dispatch-server"))
                .when()
                .body(command.toByteArray())
                .contentType(ContentTypes.PROTOBUF)
                .post("/alert/send")
                .then().statusCode(204);

        assertEquals(command, mockAlertServer.lastSentAlert());
    }

    @Test
    public void testSendEndpoint_authenticatedAsAlertReceiver() {
        var command = SendAlertCommand.newBuilder().build();
        given()
                .auth().oauth2(AlertTokens.getAlertReceiverAccessToken(123L))
                .when()
                .body(command.toByteArray())
                .contentType(ContentTypes.PROTOBUF)
                .post("/alert/send")
                .then().statusCode(403);

        assertTrue(mockAlertServer.sentAlerts().isEmpty());
    }

    @Test
    public void testSendEndpoint_unauthenticated() {
        var command = SendAlertCommand.newBuilder().build();
        given()
                .when()
                .body(command.toByteArray())
                .contentType(ContentTypes.PROTOBUF)
                .post("/alert/send")
                .then().statusCode(401);

        assertTrue(mockAlertServer.sentAlerts().isEmpty());
    }
}
