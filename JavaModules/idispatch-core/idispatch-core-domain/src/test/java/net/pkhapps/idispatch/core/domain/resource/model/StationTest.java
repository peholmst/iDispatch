package net.pkhapps.idispatch.core.domain.resource.model;

import net.pkhapps.idispatch.core.domain.common.AggregateRootTestBase;
import net.pkhapps.idispatch.core.domain.organization.OrganizationId;
import org.testng.annotations.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Unit test for {@link Station}.
 */
public class StationTest extends AggregateRootTestBase {

    @Test
    @Override
    public void initialState_newlyCreatedAggregateIsInValidState() {
        final var id = StationId.createRandom();
        final var organization = OrganizationId.createRandom();
        final var name = "My station";
        final var station = new Station(id, organization, name);

        assertThat(station.id()).isEqualTo(id);
        assertThat(station.owningOrganization()).isEqualTo(organization);
        assertThat(station.name()).isEqualTo(name);
        assertThat(station.activationState().isActive()).isTrue();
        assertThat(station.location()).isEmpty();
        assertThat(station.municipality()).isEmpty();
        assertThat(station.streetAddress()).isEmpty();
    }
}
