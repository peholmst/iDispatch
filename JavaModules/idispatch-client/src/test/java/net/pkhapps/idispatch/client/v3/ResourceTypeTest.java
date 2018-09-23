package net.pkhapps.idispatch.client.v3;

import net.pkhapps.idispatch.client.v3.ResourceType;
import net.pkhapps.idispatch.client.v3.ResourceTypeId;
import net.pkhapps.idispatch.client.v3.test.JsonObjectTest;
import net.pkhapps.idispatch.client.v3.util.MultilingualString;
import org.junit.Test;

import static net.pkhapps.idispatch.client.v3.util.Locales.FINNISH;
import static net.pkhapps.idispatch.client.v3.util.Locales.SWEDISH;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * Unit test for {@link ResourceType}.
 */
@SuppressWarnings("WeakerAccess")
public class ResourceTypeTest extends JsonObjectTest {

    public static ResourceType createTestResourceType() {
        return new ResourceType(new ResourceTypeId(1L),
                new MultilingualString.Builder()
                        .withValue(SWEDISH, "Räddningsenhet")
                        .withValue(FINNISH, "Pelastusyksikkö")
                        .build(),
                true);
    }

    @Test
    public void serializeAndDeserialize() {
        var original = createTestResourceType();
        var json = getGson().toJson(original);
        System.out.println(json);
        var deserialized = getGson().fromJson(json, ResourceType.class);
        assertThat(deserialized).isNotSameAs(original);
        assertThat(deserialized).isEqualTo(original);
    }
}
