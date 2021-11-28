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

import io.quarkus.websockets.client.runtime.WebSocketPrincipal;
import net.pkhapps.idispatch.gateway.alert.AlertServer;
import net.pkhapps.idispatch.messages.alert.Alert;
import net.pkhapps.idispatch.messages.identifiers.AlertReceiverId;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PreDestroy;
import javax.annotation.security.RolesAllowed;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.concurrent.*;

/**
 * Websocket for sending alerts to subscribing Alert Receivers. The socket perform authentication and authorization,
 * then delegates to an {@link AlertServer}.
 */
@ServerEndpoint("/alert/receiver/{receiverId}")
@ApplicationScoped
@RolesAllowed(AlertRoles.ROLE_RECEIVER)
public class AlertReceiverSocket {

    private static final Logger log = LoggerFactory.getLogger(AlertReceiverSocket.class);
    private final int maxConnections;
    private final ConcurrentMap<Long, ReceiverSession> sessions = new ConcurrentHashMap<>();
    private final AlertServer alertServer;
    private final ScheduledExecutorService pingExecutor;

    @Inject
    public AlertReceiverSocket(AlertServer alertServer,
                               @ConfigProperty(name = "alert.receivers.ping-interval-seconds", defaultValue = "30") int pingIntervalSeconds,
                               @ConfigProperty(name = "alert.receivers.max-connections", defaultValue = "1000") int maxConnections) {
        this.alertServer = alertServer;
        this.maxConnections = maxConnections;
        log.info("Pinging receivers every {} seconds", pingIntervalSeconds);
        log.info("Accepting max {} connections", maxConnections);
        log.info("Starting ping thread");
        pingExecutor = Executors.newSingleThreadScheduledExecutor();
        pingExecutor.scheduleWithFixedDelay(this::pingAll, pingIntervalSeconds, pingIntervalSeconds, TimeUnit.SECONDS);
    }

    @PreDestroy
    public void shutdown() {
        log.info("Closing all open sessions");
        sessions.forEach((id, session) -> session.closeBecauseOfShutdown());
        log.info("Shutting down ping thread");
        pingExecutor.shutdown();
    }

    private void pingAll() {
        sessions.forEach((id, session) -> session.ping());
    }

    @OnOpen
    public void onOpen(@PathParam("receiverId") Long receiverId, Session session) throws IOException {
        var securityIdentity = ((WebSocketPrincipal) session.getUserPrincipal()).getSecurityIdentity();
        if (!securityIdentity.hasRole(AlertRoles.receiverSpecificRole(receiverId))) {
            log.warn("Receiver [{}] tried to connect as invalid user ({})", receiverId, securityIdentity.getPrincipal());
            session.close(new CloseReason(CloseReason.CloseCodes.GOING_AWAY, null));
            return;
        }
        var existing = sessions.remove(receiverId);
        if (existing != null) {
            log.info("Receiver [{}] is trying to connect while already being connected, closing previous connection",
                    receiverId);
            existing.closeBecauseOfAnotherConnection();
        }
        if (sessions.size() >= maxConnections) {
            log.warn("Maximum number of connections reached ({}), cannot accept receiver [{}]", sessions.size(),
                    receiverId);
            session.close(new CloseReason(CloseReason.CloseCodes.TRY_AGAIN_LATER, "Too many connections"));
        } else {
            sessions.put(receiverId, new ReceiverSession(session, receiverId));
            log.info("Receiver [{}] connected", receiverId);
        }
    }

    @OnClose
    public void onClose(@PathParam("receiverId") Long receiverId, CloseReason reason) {
        var session = sessions.remove(receiverId);
        if (session != null) {
            log.info("Receiver [{}] disconnected because of [{}]", receiverId, reason);
            session.unsubscribe();
        }
    }

    @OnError
    public void onError(@PathParam("receiverId") Long receiverId, Throwable error) {
        var session = sessions.remove(receiverId);
        if (session != null) {
            log.warn("Receiver [" + receiverId + "] disconnected with error", error);
            session.unsubscribe();
        }
    }

    @OnMessage
    public void onPongMessage(@PathParam("receiverId") Long receiverId, PongMessage message) {
        var session = sessions.get(receiverId);
        if (session != null) {
            log.debug("Received pong from [{}]", receiverId);
            session.pong(message.getApplicationData());
        }
    }

    private class ReceiverSession {
        private final Session session;
        private final Long receiverId;
        private final AlertServer.Subscription subscription;

        @SuppressWarnings("CdiInjectionPointsInspection")
        ReceiverSession(Session session, Long receiverId) {
            this.session = session;
            this.receiverId = receiverId;
            this.subscription = alertServer.subscribeToAlerts(AlertReceiverId.newBuilder().setId(receiverId).build(),
                    this::onAlert);
        }

        private void onAlert(Alert alert) {
            log.debug("Sending alert ({}) to receiver [{}]", alert, receiverId);
            session.getAsyncRemote().sendBinary(alert.toByteString().asReadOnlyByteBuffer(), result -> {
                if (result.getException() != null) {
                    log.error("Unable to send alert to receiver [" + receiverId + "]", result.getException());
                }
            });
        }

        private void ping() {
            try {
                var data = ByteBuffer.allocate(Long.BYTES);
                data.putLong(System.currentTimeMillis());
                log.debug("Sending ping to receiver [{}]", receiverId);
                session.getAsyncRemote().sendPing(data);
            } catch (Exception ex) {
                log.error("Error sending ping to receiver [" + receiverId + "]", ex);
            }
        }

        private void closeBecauseOfShutdown() {
            subscription.unsubscribe();
            try {
                session.close(new CloseReason(CloseReason.CloseCodes.GOING_AWAY, "Server shutting down"));
            } catch (IOException ex) {
                log.error("Error closing receiver [" + receiverId + "] when shutting down", ex);
            }
        }

        private void closeBecauseOfAnotherConnection() throws IOException {
            subscription.unsubscribe();
            try {
                session.close(new CloseReason(CloseReason.CloseCodes.NO_STATUS_CODE, "Another receiver connected"));
            } catch (IOException ex) {
                log.error("Error closing receiver [" + receiverId + "] when another receiver connected");
                throw ex;
            }
        }

        private void unsubscribe() {
            subscription.unsubscribe();
        }

        @SuppressWarnings("unused")
        private void pong(ByteBuffer applicationData) {
            subscription.pong();
        }
    }
}
