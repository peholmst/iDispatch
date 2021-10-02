/*
 * iDispatch Dispatch Server
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

package net.pkhapps.idispatch.dispatch.server.domain.incidenttype;

import net.pkhapps.idispatch.dispatch.server.domain.incidenttype.api.*;
import net.pkhapps.idispatch.dispatch.server.util.Locales;
import net.pkhapps.idispatch.dispatch.server.util.MultilingualStringLiteral;
import org.axonframework.test.aggregate.AggregateTestFixture;
import org.axonframework.test.aggregate.FixtureConfiguration;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class IncidentTypeTest {

    private FixtureConfiguration<IncidentType> fixture;

    @BeforeEach
    public void setUp() {
        fixture = new AggregateTestFixture<>(IncidentType.class);
    }

    @Test
    void createIncidentType() {
        var id = IncidentTypeId.randomIncidentTypeId();
        var code = "402";
        var description = MultilingualStringLiteral.fromBilingualString(
                Locales.FINNISH, "rakennuspalo: pieni",
                Locales.SWEDISH, "byggnadsbrand: liten"
        );
        fixture.givenNoPriorActivity()
                .when(new CreateIncidentType(id, code, description))
                .expectSuccessfulHandlerExecution()
                .expectEvents(new IncidentTypeCreated(id, code, description));
    }

    @Test
    void updateIncidentType() {
        var id = IncidentTypeId.randomIncidentTypeId();
        var description = MultilingualStringLiteral.fromBilingualString(
                Locales.FINNISH, "Rakennuspalo: keskisuuri",
                Locales.SWEDISH, "Byggnadsbrand: medelstor"
        );
        fixture.given(new IncidentTypeCreated(id, "402", MultilingualStringLiteral.fromMonolingualString("rakennuspalo: keskisuuri")))
                .when(new UpdateIncidentType(id, description))
                .expectSuccessfulHandlerExecution()
                .expectEvents(new IncidentTypeUpdated(id, description));
    }

    @Test
    void updateIncidentType_noStateChanges() {
        var id = IncidentTypeId.randomIncidentTypeId();
        var description = MultilingualStringLiteral.fromMonolingualString("rakennuspalo: keskisuuri");
        fixture.given(new IncidentTypeCreated(id, "402", description))
                .when(new UpdateIncidentType(id, description))
                .expectSuccessfulHandlerExecution()
                .expectNoEvents();
    }
}
