package net.pkhapps.idispatch.client.v3.type;

import net.pkhapps.idispatch.client.v3.test.JsonObjectTest;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Unit test for {@link Resource}.
 */
@SuppressWarnings("WeakerAccess")
public class ResourceTest extends JsonObjectTest {

    public static Resource createTestResource() {
        return new Resource(new ResourceId(1L),
                ResourceTypeTest.createTestResourceType(),
                "RTEST101",
                StationTest.createTestStation(),
                true);
    }

    @Test
    public void serializeAndDeserialize() {
        var original = createTestResource();
        var json = getGson().toJson(original);
        System.out.println(json);
        var deserialized = getGson().fromJson(json, Resource.class);
        assertThat(deserialized).isNotSameAs(original);
        assertThat(deserialized).isEqualTo(original);
    }
}
