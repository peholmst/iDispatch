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
package net.pkhapps.idispatch.alert.server.application.ports.dispatcher;

import net.pkhapps.idispatch.alert.server.data.AlertId;
import net.pkhapps.idispatch.alert.server.data.IncidentIdentifier;
import net.pkhapps.idispatch.alert.server.data.ResourceIdentifier;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class AlertStatusTest {

    @Test
    void build_minimumData_successfullyBuilt() {
        var alertId = AlertId.randomAlertId();
        var alertSent = Instant.now();
        var resourceId = ResourceIdentifier.fromString("RVS911");
        var status = AlertStatus.builder().withAlertId(alertId).withAlertedResource(resourceId, alertSent).build();
        assertThat(status.getAlertId()).isEqualTo(alertId);
        assertThat(status.getIncidentIdentifier()).isEmpty();
        assertThat(status.getResources()).containsOnlyKeys(resourceId);
    }

    @Test
    void build_withIncidentIdentifier_successfullyBuilt() {
        var incidentId = IncidentIdentifier.fromString(UUID.randomUUID().toString());
        var status = AlertStatus.builder().withAlertId(AlertId.randomAlertId()).withIncidentIdentifier(incidentId)
                .withAlertedResource(ResourceIdentifier.fromString("RVS911"), Instant.now()).build();
        assertThat(status.getIncidentIdentifier()).contains(incidentId);
    }

    @Test
    void build_noResources_exceptionThrown() {
        var builder = AlertStatus.builder().withAlertId(AlertId.randomAlertId());
        assertThatThrownBy(builder::build).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void build_withAlertedResource_successfullyBuilt() {
        var alertSent = Instant.now();
        var resourceId = ResourceIdentifier.fromString("RVS911");
        var status = AlertStatus.builder().withAlertId(AlertId.randomAlertId())
                .withAlertedResource(resourceId, alertSent).build().getResources().get(resourceId);
        assertThat(status.getResourceIdentifier()).isEqualTo(resourceId);
        assertThat(status.getAlertSent()).contains(alertSent);
        assertThat(status.getAlertDelivered()).isEmpty();
        assertThat(status.isAlertDelivered()).isFalse();
        assertThat(status.isAlertSent()).isTrue();
        assertThat(status.isResourceUnknown()).isFalse();
        assertThat(status.isTimedOut()).isFalse();
    }

    @Test
    void build_withSuccessfullyAlertedResource_successfullyBuilt() {
        var alertSent = Instant.now();
        var alertDelivered = alertSent.plusSeconds(1);
        var resourceId = ResourceIdentifier.fromString("RVS911");
        var status = AlertStatus.builder().withAlertId(AlertId.randomAlertId())
                .withSuccessfullyAlertedResource(resourceId, alertSent, alertDelivered).build().getResources()
                .get(resourceId);
        assertThat(status.getAlertDelivered()).contains(alertDelivered);
        assertThat(status.isAlertDelivered()).isTrue();
        assertThat(status.isAlertSent()).isTrue();
        assertThat(status.isResourceUnknown()).isFalse();
        assertThat(status.isTimedOut()).isFalse();
    }

    @Test
    void build_withTimedOutResource_successfullyBuilt() {
        var alertSent = Instant.now();
        var resourceId = ResourceIdentifier.fromString("RVS911");
        var status = AlertStatus.builder().withAlertId(AlertId.randomAlertId())
                .withTimedOutResource(resourceId, alertSent).build().getResources().get(resourceId);
        assertThat(status.getAlertDelivered()).isEmpty();
        assertThat(status.isAlertDelivered()).isFalse();
        assertThat(status.isAlertSent()).isTrue();
        assertThat(status.isResourceUnknown()).isFalse();
        assertThat(status.isTimedOut()).isTrue();
    }

    @Test
    void build_withUnknownResource_successfullyBuilt() {
        var resourceId = ResourceIdentifier.fromString("RVS911");
        var status = AlertStatus.builder().withAlertId(AlertId.randomAlertId()).withUnknownResource(resourceId).build()
                .getResources().get(resourceId);
        assertThat(status.getAlertSent()).isEmpty();
        assertThat(status.getAlertDelivered()).isEmpty();
        assertThat(status.isAlertDelivered()).isFalse();
        assertThat(status.isAlertSent()).isFalse();
        assertThat(status.isResourceUnknown()).isTrue();
        assertThat(status.isTimedOut()).isFalse();
    }
}
