package net.pkhapps.idispatch.core.domain.resource.model;

import net.pkhapps.idispatch.core.domain.common.AggregateRootTestBase;
import net.pkhapps.idispatch.core.domain.organization.OrganizationId;
import org.testng.annotations.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Unit test for {@link Resource}.
 */
public class ResourceTest extends AggregateRootTestBase {

    @Test
    @Override
    public void initialState_newlyCreatedAggregateIsInValidState() {
        final var id = ResourceId.createRandom();
        final var organization = OrganizationId.createRandom();
        final var callSign = CallSign.createFromString("RVSPG31");
        final var type = ResourceTypeId.createRandom();
        final var resource = new Resource(id, organization, callSign, type);

        assertThat(resource.id()).isEqualTo(id);
        assertThat(resource.owningOrganization()).isEqualTo(organization);
        assertThat(resource.callSign()).isEqualTo(callSign);
        assertThat(resource.type()).isEqualTo(type);
        assertThat(resource.activationState().isActive()).isTrue();
        assertThat(resource.station()).isEmpty();
        assertThat(resource.comment()).isEmpty();
    }
}
