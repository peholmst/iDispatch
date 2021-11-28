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

import io.quarkus.test.Mock;
import net.pkhapps.idispatch.messages.alert.Alert;
import net.pkhapps.idispatch.messages.alert.commands.AcknowledgeAlertCommand;
import net.pkhapps.idispatch.messages.alert.commands.SendAlertCommand;
import net.pkhapps.idispatch.messages.identifiers.AlertReceiverId;

import javax.enterprise.context.ApplicationScoped;
import java.time.Instant;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;

/**
 * Mock implementation of {@link AlertServer} intended for testing only.
 */
@Mock
@ApplicationScoped
public class MockAlertServer implements AlertServer {

    private final LinkedBlockingDeque<SendAlertCommand> sentAlerts = new LinkedBlockingDeque<>();
    private final LinkedBlockingDeque<AcknowledgeAlertCommand> acknowledgedAlerts = new LinkedBlockingDeque<>();
    private final ConcurrentMap<AlertReceiverId, SubscriptionImpl> subscriptions = new ConcurrentHashMap<>();

    @Override
    public void sendAlert(SendAlertCommand command) {
        sentAlerts.add(command);
    }

    @Override
    public void acknowledgeAlert(AcknowledgeAlertCommand command) {
        acknowledgedAlerts.add(command);
    }

    @Override
    public Subscription subscribeToAlerts(AlertReceiverId receiverId, Consumer<Alert> alertConsumer) {
        var subscription = new SubscriptionImpl(receiverId, alertConsumer);
        subscriptions.put(receiverId, subscription);
        synchronized (this) {
            notify();
        }
        return subscription;
    }

    public void awaitSubscription(AlertReceiverId receiverId, int timeoutSeconds) throws Exception {
        if (subscriptions.containsKey(receiverId)) {
            return;
        }
        synchronized (this) {
            wait(timeoutSeconds * 1000L);
        }
        if (!subscriptions.containsKey(receiverId)) {
            throw new IllegalStateException("Did not receive subscription for " + receiverId);
        }
    }

    public boolean isSubscribed(AlertReceiverId receiverId) {
        return subscriptions.containsKey(receiverId);
    }

    public void awaitPong(AlertReceiverId receiverId, int timeoutSeconds) throws Exception {
        var now = Instant.now();
        if (hasBeenPongedAfter(receiverId, now))
            return;
        synchronized (this) {
            wait(timeoutSeconds * 1000L);
        }
        if (!hasBeenPongedAfter(receiverId, now)) {
            throw new IllegalStateException("Did not receive a pong for " + receiverId);
        }
    }

    private boolean hasBeenPongedAfter(AlertReceiverId receiverId, Instant instant) {
        var subscription = subscriptions.get(receiverId);
        if (subscription == null) {
            return false;
        }
        var lastSeen = subscription.lastSeen.get();
        return lastSeen != null && lastSeen.isAfter(instant);
    }

    public void sendAlert(AlertReceiverId receiverId, Alert alert) {
        var subscription = subscriptions.get(receiverId);
        if (subscription != null) {
            subscription.sendAlert(alert);
        }
    }

    public List<AcknowledgeAlertCommand> acknowledgedAlerts() {
        return acknowledgedAlerts.stream().toList();
    }

    public AcknowledgeAlertCommand lastAcknowledgedAlert() {
        return acknowledgedAlerts.peekLast();
    }

    public List<SendAlertCommand> sentAlerts() {
        return sentAlerts.stream().toList();
    }

    public SendAlertCommand lastSentAlert() {
        return sentAlerts.peekLast();
    }

    public void reset() {
        sentAlerts.clear();
        acknowledgedAlerts.clear();
    }

    private class SubscriptionImpl implements Subscription {

        private final AlertReceiverId receiverId;
        private final Consumer<Alert> alertConsumer;
        private final AtomicReference<Instant> lastSeen = new AtomicReference<>();

        SubscriptionImpl(AlertReceiverId receiverId, Consumer<Alert> alertConsumer) {
            this.receiverId = receiverId;
            this.alertConsumer = alertConsumer;
        }

        @Override
        public void unsubscribe() {
            subscriptions.remove(receiverId);
        }

        @Override
        public void pong() {
            lastSeen.set(Instant.now());
            synchronized (MockAlertServer.this) {
                MockAlertServer.this.notify();
            }
        }

        void sendAlert(Alert alert) {
            alertConsumer.accept(alert);
        }
    }
}
