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
import io.quarkus.test.common.http.TestHTTPResource;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.oidc.server.OidcWiremockTestResource;
import net.pkhapps.idispatch.gateway.alert.MockAlertServer;
import net.pkhapps.idispatch.messages.alert.Alert;
import net.pkhapps.idispatch.messages.identifiers.AlertReceiverId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;
import javax.websocket.*;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

@QuarkusTest
@QuarkusTestResource(OidcWiremockTestResource.class)
public class AlertReceiverSocketTest {

    private static final LinkedBlockingDeque<Alert> ALERTS = new LinkedBlockingDeque<>();
    private static final LinkedBlockingDeque<CloseReason> CLOSE_REASONS = new LinkedBlockingDeque<>();

    @TestHTTPResource("/alert/receiver/123")
    URI uri;

    @Inject
    MockAlertServer mockAlertServer;

    @BeforeEach
    public void setUp() {
        mockAlertServer.reset();
        ALERTS.clear();
        CLOSE_REASONS.clear();
    }

    @Test
    public void testConnection_authenticatedAsCorrectReceiver() throws Exception {
        var receiverId = AlertReceiverId.newBuilder().setId(123L).build();
        var alert = Alert.newBuilder().build();
        try (var session = ContainerProvider.getWebSocketContainer()
                .connectToServer(Client.class,
                        authorizationConfiguration(AlertTokens.getAlertReceiverAccessToken(receiverId.getId())),
                        uri)) {
            mockAlertServer.awaitSubscription(receiverId, 10);
            mockAlertServer.sendAlert(receiverId, alert);
            assertEquals(alert, ALERTS.poll(10, TimeUnit.SECONDS));
            mockAlertServer.awaitPong(receiverId, 10);
        }
    }

    @Test
    public void testConnection_authenticatedAsIncorrectReceiver() throws Exception {
        try (var session = ContainerProvider.getWebSocketContainer()
                .connectToServer(Client.class,
                        authorizationConfiguration(AlertTokens.getAlertReceiverAccessToken(456L)),
                        uri)) {
            assertEquals(CloseReason.CloseCodes.GOING_AWAY, CLOSE_REASONS.poll(10, TimeUnit.SECONDS).getCloseCode());
            assertFalse(mockAlertServer.isSubscribed(AlertReceiverId.newBuilder().setId(123L).build()));
        }
    }

    @Test
    public void testConnection_authenticatedAsDispatcher() throws Exception {
        try (var session = ContainerProvider.getWebSocketContainer()
                .connectToServer(Client.class,
                        authorizationConfiguration(AlertTokens.getDispatcherAccessToken("dispatcher-system")),
                        uri)) {
            assertEquals(CloseReason.CloseCodes.GOING_AWAY, CLOSE_REASONS.poll(10, TimeUnit.SECONDS).getCloseCode());
            assertFalse(mockAlertServer.isSubscribed(AlertReceiverId.newBuilder().setId(123L).build()));
        }
    }

    @Test
    public void testConnection_unauthenticated() throws Exception {
        try (var session = ContainerProvider.getWebSocketContainer()
                .connectToServer(Client.class, ClientEndpointConfig.Builder.create().build(), uri)) {
            assertEquals(CloseReason.CloseCodes.GOING_AWAY, CLOSE_REASONS.poll(10, TimeUnit.SECONDS).getCloseCode());
            assertFalse(mockAlertServer.isSubscribed(AlertReceiverId.newBuilder().setId(123L).build()));
        }
    }

    private ClientEndpointConfig authorizationConfiguration(String accessToken) {
        return ClientEndpointConfig.Builder.create()
                .configurator(new ClientEndpointConfig.Configurator() {
                    @Override
                    public void beforeRequest(Map<String, List<String>> headers) {
                        headers.put("Authorization", List.of("Bearer " + accessToken));
                    }
                })
                .build();
    }

    public static class Client extends Endpoint {
        @Override
        public void onOpen(Session session, EndpointConfig config) {
            session.addMessageHandler(InputStream.class, new MessageHandler.Whole<InputStream>() {
                @Override
                public void onMessage(InputStream message) {
                    try {
                        var alert = Alert.newBuilder().mergeFrom(message).build();
                        ALERTS.push(alert);
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                }
            });
        }

        @Override
        public void onClose(Session session, CloseReason closeReason) {
            CLOSE_REASONS.push(closeReason);
        }
    }
}
