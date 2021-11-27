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
import io.smallrye.jwt.build.Jwt;
import net.pkhapps.idispatch.gateway.alert.MockAlertServer;
import net.pkhapps.idispatch.gateway.protobuf.ContentTypes;
import net.pkhapps.idispatch.messages.alert.commands.AcknowledgeAlertCommand;
import net.pkhapps.idispatch.messages.alert.commands.SendAlertCommand;
import net.pkhapps.idispatch.messages.identifiers.AlertReceiverId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;
import java.util.Set;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@QuarkusTest
@QuarkusTestResource(OidcWiremockTestResource.class)
public class AlertControllerTest {

    @Inject
    MockAlertServer mockAlertPort;

    @BeforeEach
    public void setUp() {
        mockAlertPort.reset();
    }

    @Test
    public void testAckEndpoint_authenticatedAsCorrectReceiver() {
        var command = AcknowledgeAlertCommand.newBuilder()
                .setReceiver(AlertReceiverId.newBuilder().setId(123L).build())
                .build();
        given()
                .auth().oauth2(getAlertReceiverAccessToken(123L))
                .when()
                .body(command.toByteArray())
                .contentType(ContentTypes.PROTOBUF)
                .post("/alert/ack")
                .then().statusCode(204);

        assertEquals(command, mockAlertPort.lastAcknowledgedAlert());
    }

    @Test
    public void testAckEndpoint_authenticatedAsIncorrectReceiver() {
        var command = AcknowledgeAlertCommand.newBuilder()
                .setReceiver(AlertReceiverId.newBuilder().setId(123L).build())
                .build();
        given()
                .auth().oauth2(getAlertReceiverAccessToken(4546L))
                .when()
                .body(command.toByteArray())
                .contentType(ContentTypes.PROTOBUF)
                .post("/alert/ack")
                .then().statusCode(403);

        assertTrue(mockAlertPort.acknowledgedAlerts().isEmpty());
    }

    @Test
    public void testAckEndpoint_authenticatedAsDispatcher() {
        var command = AcknowledgeAlertCommand.newBuilder()
                .setReceiver(AlertReceiverId.newBuilder().setId(123L).build())
                .build();
        given()
                .auth().oauth2(getDispatcherAccessToken("dispatch-server"))
                .when()
                .body(command.toByteArray())
                .contentType(ContentTypes.PROTOBUF)
                .post("/alert/ack")
                .then().statusCode(403);

        assertTrue(mockAlertPort.acknowledgedAlerts().isEmpty());
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

        assertTrue(mockAlertPort.acknowledgedAlerts().isEmpty());
    }

    @Test
    public void testAckEndpoint_missingReceiverId() {
        var command = AcknowledgeAlertCommand.newBuilder().build();
        given()
                .auth().oauth2(getAlertReceiverAccessToken(4546L))
                .when()
                .body(command.toByteArray())
                .contentType(ContentTypes.PROTOBUF)
                .post("/alert/ack")
                .then().statusCode(400);

        assertTrue(mockAlertPort.acknowledgedAlerts().isEmpty());
    }

    @Test
    public void testSendEndpoint_authenticatedAsDispatcher() {
        var command = SendAlertCommand.newBuilder().build();
        given()
                .auth().oauth2(getDispatcherAccessToken("dispatch-server"))
                .when()
                .body(command.toByteArray())
                .contentType(ContentTypes.PROTOBUF)
                .post("/alert/send")
                .then().statusCode(204);

        assertEquals(command, mockAlertPort.lastSentAlert());
    }

    @Test
    public void testSendEndpoint_authenticatedAsAlertReceiver() {
        var command = SendAlertCommand.newBuilder().build();
        given()
                .auth().oauth2(getAlertReceiverAccessToken(123L))
                .when()
                .body(command.toByteArray())
                .contentType(ContentTypes.PROTOBUF)
                .post("/alert/send")
                .then().statusCode(403);

        assertTrue(mockAlertPort.sentAlerts().isEmpty());
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

        assertTrue(mockAlertPort.sentAlerts().isEmpty());
    }

    private String getAlertReceiverAccessToken(long alertReceiverId) {
        return Jwt.preferredUserName("alert-receiver-" + alertReceiverId)
                .groups(Set.of(AlertRoles.ROLE_RECEIVER, AlertRoles.receiverSpecificRole(alertReceiverId)))
                .issuer("https://server.example.com")
                .audience("https://service.example.com")
                .sign();
    }

    private String getDispatcherAccessToken(String username) {
        return Jwt.preferredUserName(username)
                .groups(AlertRoles.ROLE_DISPATCHER)
                .issuer("https://server.example.com")
                .audience("https://service.example.com")
                .sign();
    }
}
