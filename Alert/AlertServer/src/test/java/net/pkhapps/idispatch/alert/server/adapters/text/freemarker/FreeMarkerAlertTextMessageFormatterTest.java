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

package net.pkhapps.idispatch.alert.server.adapters.text.freemarker;

import net.pkhapps.idispatch.alert.server.application.text.AlertTextMessageFormatter;
import net.pkhapps.idispatch.alert.server.data.Address;
import net.pkhapps.idispatch.alert.server.data.AlertTestData;
import net.pkhapps.idispatch.alert.server.data.MultilingualStringLiteral;
import net.pkhapps.idispatch.alert.server.data.WrappingValueObject;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class FreeMarkerAlertTextMessageFormatterTest {

    private final AlertTextMessageFormatter formatter = new FreeMarkerAlertTextMessageFormatter();

    @Test
    void formatAlertMessage_address_intersection() {
        var alert = AlertTestData.createTestAlert(Address.fromIntersection(
                MultilingualStringLiteral.fromBilingualString(AlertTestData.SWEDISH, "Skärgårdsvägen", AlertTestData.FINNISH, "Saaristotie"),
                MultilingualStringLiteral.fromBilingualString(AlertTestData.SWEDISH, "Sydmovägen", AlertTestData.FINNISH, "Sydmontie")
        ));
        assertThat(formatter.formatAlertMessage(alert, "${alert.address}")).isEqualTo("Skärgårdsvägen || Sydmovägen");
        assertThat(formatter.formatAlertMessage(alert, "${alert.address.sv}")).isEqualTo("Skärgårdsvägen || Sydmovägen");
        assertThat(formatter.formatAlertMessage(alert, "${alert.address.fi}")).isEqualTo("Saaristotie || Sydmontie");
        assertThat(formatter.formatAlertMessage(alert, "${alert.address.road}")).isEqualTo("Skärgårdsvägen");
        assertThat(formatter.formatAlertMessage(alert, "${alert.address.road.sv}")).isEqualTo("Skärgårdsvägen");
        assertThat(formatter.formatAlertMessage(alert, "${alert.address.road.fi}")).isEqualTo("Saaristotie");
        assertThat(formatter.formatAlertMessage(alert, "${alert.address.intersectingRoad}")).isEqualTo("Sydmovägen");
        assertThat(formatter.formatAlertMessage(alert, "${alert.address.intersectingRoad.sv}")).isEqualTo("Sydmovägen");
        assertThat(formatter.formatAlertMessage(alert, "${alert.address.intersectingRoad.fi}")).isEqualTo("Sydmontie");
        assertThat(formatter.formatAlertMessage(alert, "${alert.address.comments}")).isEmpty();
        assertThat(formatter.formatAlertMessage(alert, "${alert.address.number}")).isEmpty();
    }

    @Test
    void formatAlertMessage_address_unnamed() {
        var alert = AlertTestData.createTestAlert(Address.fromUnnamedLocation("comments"));
        assertThat(formatter.formatAlertMessage(alert, "${alert.address}")).isEqualTo("comments");
        assertThat(formatter.formatAlertMessage(alert, "${alert.address.sv}")).isEqualTo("comments");
        assertThat(formatter.formatAlertMessage(alert, "${alert.address.fi}")).isEqualTo("comments");
        assertThat(formatter.formatAlertMessage(alert, "${alert.address.road}")).isEmpty();
        assertThat(formatter.formatAlertMessage(alert, "${alert.address.road.sv}")).isEmpty();
        assertThat(formatter.formatAlertMessage(alert, "${alert.address.road.fi}")).isEmpty();
        assertThat(formatter.formatAlertMessage(alert, "${alert.address.intersectingRoad}")).isEmpty();
        assertThat(formatter.formatAlertMessage(alert, "${alert.address.intersectingRoad.sv}")).isEmpty();
        assertThat(formatter.formatAlertMessage(alert, "${alert.address.intersectingRoad.fi}")).isEmpty();
        assertThat(formatter.formatAlertMessage(alert, "${alert.address.comments}")).isEqualTo("comments");
        assertThat(formatter.formatAlertMessage(alert, "${alert.address.number}")).isEmpty();
    }

    @Test
    void formatAlertMessage_address_inexactStreetAddress() {
        var alert = AlertTestData.createTestAlert(Address.fromStreetAddress(
                MultilingualStringLiteral.fromBilingualString(AlertTestData.SWEDISH, "Köpmansgatan", AlertTestData.FINNISH, "Kauppiaskatu"),
                null));
        assertThat(formatter.formatAlertMessage(alert, "${alert.address}")).isEqualTo("Köpmansgatan");
        assertThat(formatter.formatAlertMessage(alert, "${alert.address.sv}")).isEqualTo("Köpmansgatan");
        assertThat(formatter.formatAlertMessage(alert, "${alert.address.fi}")).isEqualTo("Kauppiaskatu");
        assertThat(formatter.formatAlertMessage(alert, "${alert.address.road}")).isEqualTo("Köpmansgatan");
        assertThat(formatter.formatAlertMessage(alert, "${alert.address.road.sv}")).isEqualTo("Köpmansgatan");
        assertThat(formatter.formatAlertMessage(alert, "${alert.address.road.fi}")).isEqualTo("Kauppiaskatu");
        assertThat(formatter.formatAlertMessage(alert, "${alert.address.intersectingRoad}")).isEmpty();
        assertThat(formatter.formatAlertMessage(alert, "${alert.address.intersectingRoad.sv}")).isEmpty();
        assertThat(formatter.formatAlertMessage(alert, "${alert.address.intersectingRoad.fi}")).isEmpty();
        assertThat(formatter.formatAlertMessage(alert, "${alert.address.comments}")).isEmpty();
        assertThat(formatter.formatAlertMessage(alert, "${alert.address.number}")).isEmpty();
    }

    @Test
    void formatAlertMessage_address_exactStreetAddress() {
        var alert = AlertTestData.createTestAlert(Address.fromStreetAddress(
                MultilingualStringLiteral.fromBilingualString(AlertTestData.SWEDISH, "Bläsnäsvägen", AlertTestData.FINNISH, "Bläsnäsintie"),
                "7"));
        assertThat(formatter.formatAlertMessage(alert, "${alert.address}")).isEqualTo("Bläsnäsvägen 7");
        assertThat(formatter.formatAlertMessage(alert, "${alert.address.sv}")).isEqualTo("Bläsnäsvägen 7");
        assertThat(formatter.formatAlertMessage(alert, "${alert.address.fi}")).isEqualTo("Bläsnäsintie 7");
        assertThat(formatter.formatAlertMessage(alert, "${alert.address.road}")).isEqualTo("Bläsnäsvägen");
        assertThat(formatter.formatAlertMessage(alert, "${alert.address.road.sv}")).isEqualTo("Bläsnäsvägen");
        assertThat(formatter.formatAlertMessage(alert, "${alert.address.road.fi}")).isEqualTo("Bläsnäsintie");
        assertThat(formatter.formatAlertMessage(alert, "${alert.address.intersectingRoad}")).isEmpty();
        assertThat(formatter.formatAlertMessage(alert, "${alert.address.intersectingRoad.sv}")).isEmpty();
        assertThat(formatter.formatAlertMessage(alert, "${alert.address.intersectingRoad.fi}")).isEmpty();
        assertThat(formatter.formatAlertMessage(alert, "${alert.address.comments}")).isEmpty();
        assertThat(formatter.formatAlertMessage(alert, "${alert.address.number}")).isEqualTo("7");
    }

    @Test
    void formatAlertMessage_address_noAddress() {
        var alert = AlertTestData.createTestAlert(null);
        assertThat(formatter.formatAlertMessage(alert, "${alert.address}")).isEmpty();
        assertThat(formatter.formatAlertMessage(alert, "${alert.address.sv}")).isEmpty();
        assertThat(formatter.formatAlertMessage(alert, "${alert.address.fi}")).isEmpty();
        assertThat(formatter.formatAlertMessage(alert, "${alert.address.road}")).isEmpty();
        assertThat(formatter.formatAlertMessage(alert, "${alert.address.road.sv}")).isEmpty();
        assertThat(formatter.formatAlertMessage(alert, "${alert.address.road.fi}")).isEmpty();
        assertThat(formatter.formatAlertMessage(alert, "${alert.address.intersectingRoad}")).isEmpty();
        assertThat(formatter.formatAlertMessage(alert, "${alert.address.intersectingRoad.sv}")).isEmpty();
        assertThat(formatter.formatAlertMessage(alert, "${alert.address.intersectingRoad.fi}")).isEmpty();
        assertThat(formatter.formatAlertMessage(alert, "${alert.address.comments}")).isEmpty();
        assertThat(formatter.formatAlertMessage(alert, "${alert.address.number}")).isEmpty();
    }

    @Test
    void formatAlertMessage_municipality() {
        var alert = AlertTestData.createTestAlert();
        assertThat(formatter.formatAlertMessage(alert, "${alert.municipality}")).isEqualTo("Pargas");
        assertThat(formatter.formatAlertMessage(alert, "${alert.municipality.sv}")).isEqualTo("Pargas");
        assertThat(formatter.formatAlertMessage(alert, "${alert.municipality.fi}")).isEqualTo("Parainen");
    }

    @Test
    void formatAlertMessage_incidentTypeAndUrgency() {
        var alert = AlertTestData.createTestAlert();
        assertThat(formatter.formatAlertMessage(alert, "${alert.incidentType}")).isEqualTo("401");
        assertThat(formatter.formatAlertMessage(alert, "${alert.incidentUrgency}")).isEqualTo("B");
    }

    @Test
    void formatAlertMessage_coordinates() {
        var alert = AlertTestData.createTestAlert();
        assertThat(formatter.formatAlertMessage(alert, "${alert.coordinates.lat?string['0.#####']},${alert.coordinates.lon?string['0.#####']}")).isEqualTo("60.29857,22.30174");
    }

    @Test
    void formatAlertMessage_details() {
        var alert = AlertTestData.createTestAlert();
        assertThat(formatter.formatAlertMessage(alert, "${alert.details}")).isEqualTo(alert.details());
    }

    @Test
    void formatAlertMessage_resources() {
        var alert = AlertTestData.createTestAlert();
        assertThat(formatter.formatAlertMessage(alert, "${alert.resources?join(',')}")).isEqualTo("RVS911,RVS917");
    }

    @Test
    void formatAlertMessage_incidentIdentifier() {
        var alert = AlertTestData.createTestAlert();
        assertThat(formatter.formatAlertMessage(alert, "${alert.incidentIdentifier}")).isEqualTo(alert.incidentIdentifier().map(WrappingValueObject::unwrap).orElseThrow());
    }

    @Test
    void formatAlertMessage_alertInstant() {
        var alert = AlertTestData.createTestAlert();
        var expected = LocalDateTime
                .ofInstant(alert.alertInstant(), ZoneId.systemDefault())
                .withNano(0)
                .format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        assertThat(formatter.formatAlertMessage(alert, "${alert.alertInstant}")).isEqualTo(expected);
    }

    @Test
    void formatAlertMessage_incidentInstant() {
        var alert = AlertTestData.createTestAlert();
        var expected = LocalDateTime
                .ofInstant(alert.incidentInstant().orElseThrow(), ZoneId.systemDefault())
                .withNano(0)
                .format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        assertThat(formatter.formatAlertMessage(alert, "${alert.incidentInstant}")).isEqualTo(expected);
    }
}
