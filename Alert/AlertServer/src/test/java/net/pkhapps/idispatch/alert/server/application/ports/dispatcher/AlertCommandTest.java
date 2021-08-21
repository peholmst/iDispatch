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

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.Instant;
import java.util.UUID;

import org.junit.jupiter.api.Test;

import net.pkhapps.idispatch.alert.server.domain.model.Address;
import net.pkhapps.idispatch.alert.server.domain.model.GeoPoint;
import net.pkhapps.idispatch.alert.server.domain.model.IncidentIdentifier;
import net.pkhapps.idispatch.alert.server.domain.model.IncidentTypeCode;
import net.pkhapps.idispatch.alert.server.domain.model.IncidentUrgencyCode;
import net.pkhapps.idispatch.alert.server.domain.model.Municipality;
import net.pkhapps.idispatch.alert.server.domain.model.ResourceIdentifier;

class AlertCommandTest {

    @Test
    void build_minimumData_successfullyBuilt() {
        var incidentType = IncidentTypeCode.fromString("401");
        var incidentUrgency = IncidentUrgencyCode.fromString("B");
        var municipality = Municipality.fromMonolingualName("Municipality");
        var coordinates = GeoPoint.fromLatLon(60, 22);
        var resourceId = ResourceIdentifier.fromString("RVS911");

        var command = AlertCommand.builder().withIncidentType(incidentType).withIncidentUrgency(incidentUrgency)
                .withMunicipality(municipality).withCoordinates(coordinates).withResourceToAlert(resourceId).build();
        assertThat(command.getIncidentType()).isEqualTo(incidentType);
        assertThat(command.getIncidentUrgency()).isEqualTo(incidentUrgency);
        assertThat(command.getMunicipality()).isEqualTo(municipality);
        assertThat(command.getCoordinates()).isEqualTo(coordinates);
        assertThat(command.getAssignedResources()).containsExactly(resourceId);
        assertThat(command.getResourcesToAlert()).containsExactly(resourceId);
        assertThat(command.getAddress()).isEmpty();
        assertThat(command.getDetails()).isEmpty();
        assertThat(command.getIncidentIdentifier()).isEmpty();
        assertThat(command.getIncidentInstant()).isEmpty();
    }

    @Test
    void build_fullData_successfullyBuilt() {
        var incidentType = IncidentTypeCode.fromString("401");
        var incidentUrgency = IncidentUrgencyCode.fromString("B");
        var municipality = Municipality.fromMonolingualName("Municipality");
        var coordinates = GeoPoint.fromLatLon(60, 22);
        var resourceId = ResourceIdentifier.fromString("RVS911");
        var assignedResourceId = ResourceIdentifier.fromString("RVS921");
        var address = Address.fromMonolingualStreetAddress("Road", "Number");
        var details = "details";
        var incidentIdentifier = IncidentIdentifier.fromString(UUID.randomUUID().toString());
        var incidentInstant = Instant.now();

        var command = AlertCommand.builder().withIncidentType(incidentType).withIncidentUrgency(incidentUrgency)
                .withMunicipality(municipality).withCoordinates(coordinates).withResourceToAlert(resourceId)
                .withAssignedResource(assignedResourceId).withAddress(address).withDetails(details)
                .withIncidentIdentifier(incidentIdentifier).withIncidentInstant(incidentInstant).build();
        assertThat(command.getIncidentType()).isEqualTo(incidentType);
        assertThat(command.getIncidentUrgency()).isEqualTo(incidentUrgency);
        assertThat(command.getMunicipality()).isEqualTo(municipality);
        assertThat(command.getCoordinates()).isEqualTo(coordinates);
        assertThat(command.getAssignedResources()).containsExactlyInAnyOrder(resourceId, assignedResourceId);
        assertThat(command.getResourcesToAlert()).containsExactly(resourceId);
        assertThat(command.getAddress()).contains(address);
        assertThat(command.getDetails()).isEqualTo(details);
        assertThat(command.getIncidentIdentifier()).contains(incidentIdentifier);
        assertThat(command.getIncidentInstant()).contains(incidentInstant);
    }

    @Test
    void build_noResourcesToAlert_exceptionThrown() {
        var builder = AlertCommand.builder().withIncidentType(IncidentTypeCode.fromString("401"))
                .withIncidentUrgency(IncidentUrgencyCode.fromString("B"))
                .withMunicipality(Municipality.fromMonolingualName("Municipality"))
                .withCoordinates(GeoPoint.fromLatLon(60, 22));
        assertThatThrownBy(builder::build).isInstanceOf(IllegalArgumentException.class);
    }
}
