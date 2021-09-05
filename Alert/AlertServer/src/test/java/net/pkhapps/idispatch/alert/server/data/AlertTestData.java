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
package net.pkhapps.idispatch.alert.server.data;

import java.time.Instant;
import java.util.Locale;
import java.util.UUID;

public class AlertTestData {

    public static final Locale SWEDISH = new Locale("sv");
    public static final Locale FINNISH = new Locale("fi");

    public static Alert createTestAlert(Address address) {
        var incidentInstant = Instant.now().minusSeconds(45);
        return new Alert.Essence()
                .setIncidentIdentifier(IncidentIdentifier.fromString(UUID.randomUUID().toString()))
                .setIncidentInstant(incidentInstant)
                .setIncidentType(IncidentTypeCode.fromString("401"))
                .setIncidentUrgency(IncidentUrgencyCode.fromString("B"))
                .setMunicipality(Municipality
                        .fromName(MultilingualStringLiteral
                                .fromBilingualString(
                                        SWEDISH, "Pargas",
                                        FINNISH, "Parainen")))
                .setCoordinates(GeoPoint.fromLatLon(60.298566384, 22.301742545))
                .setAddress(address)
                .setDetails("Smoke alarm is beeping, no visible smoke or flames. Passer-by made the call. Lights are out and no-one is answering the door when ringing the doorbell.")
                .addResourceToAlert(ResourceIdentifier.fromString("RVS911"))
                .addResourceToAlert(ResourceIdentifier.fromString("RVS917"))
                .createAlert(AlertId.randomAlertId(), Instant.now());
    }

    public static Alert createTestAlert() {
        return createTestAlert(Address
                .fromStreetAddress(MultilingualStringLiteral
                                .fromBilingualString(SWEDISH, "Köpmansgatan", FINNISH, "Kauppiaskatu"),
                        "1"));
    }
}
