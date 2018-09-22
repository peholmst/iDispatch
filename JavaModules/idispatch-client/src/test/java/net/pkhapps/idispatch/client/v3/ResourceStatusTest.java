package net.pkhapps.idispatch.client.v3;

import net.pkhapps.idispatch.client.v3.base.TemporalValue;
import net.pkhapps.idispatch.client.v3.geo.CoordinateReferenceSystem;
import net.pkhapps.idispatch.client.v3.geo.GeographicLocation;
import net.pkhapps.idispatch.client.v3.test.JsonObjectTest;
import net.pkhapps.idispatch.client.v3.type.ResourceId;
import net.pkhapps.idispatch.client.v3.type.ResourceStateId;
import org.junit.Test;

import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Unit test for {@link ResourceStatus}.
 */
@SuppressWarnings("WeakerAccess")
public class ResourceStatusTest extends JsonObjectTest {

    public static ResourceStatus createTestResourceStatus() {
        return new ResourceStatus(new ResourceId(1L),
                new TemporalValue<>(new ResourceStateId(2L), Instant.now()),
                new TemporalValue<>(new GeographicLocation(CoordinateReferenceSystem.ETRS_TM35FIN, 6694191, 240528),
                        Instant.now().minusSeconds(60)),
                new TemporalValue<>(new AssignmentId(123L), Instant.now().minusSeconds(75)));
    }

    @Test
    public void serializeAndDeserialize() {
        var original = createTestResourceStatus();
        var json = getGson().toJson(original);
        System.out.println(json);
        var deserialized = getGson().fromJson(json, ResourceStatus.class);
        assertThat(deserialized).isNotSameAs(original);
        assertThat(deserialized).isEqualTo(original);
    }
}
