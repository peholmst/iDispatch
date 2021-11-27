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
import net.pkhapps.idispatch.messages.alert.commands.AcknowledgeAlertCommand;
import net.pkhapps.idispatch.messages.alert.commands.SendAlertCommand;

import javax.enterprise.context.ApplicationScoped;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * TODO Document me
 */
@Mock
@ApplicationScoped
public class MockAlertServer implements AlertServer {

    private final List<SendAlertCommand> sentAlerts = new LinkedList<>();
    private final List<AcknowledgeAlertCommand> acknowledgedAlerts = new LinkedList<>();

    @Override
    public synchronized void sendAlert(SendAlertCommand command) {
        sentAlerts.add(command);
    }

    @Override
    public synchronized void acknowledgeAlert(AcknowledgeAlertCommand command) {
        acknowledgedAlerts.add(command);
    }

    public synchronized List<AcknowledgeAlertCommand> acknowledgedAlerts() {
        return Collections.unmodifiableList(acknowledgedAlerts);
    }

    public synchronized AcknowledgeAlertCommand lastAcknowledgedAlert() {
        return acknowledgedAlerts.get(acknowledgedAlerts.size() - 1);
    }

    public synchronized List<SendAlertCommand> sentAlerts() {
        return Collections.unmodifiableList(sentAlerts);
    }

    public synchronized SendAlertCommand lastSentAlert() {
        return sentAlerts.get(sentAlerts.size() - 1);
    }

    public synchronized void reset() {
        sentAlerts.clear();
        acknowledgedAlerts.clear();
    }
}
