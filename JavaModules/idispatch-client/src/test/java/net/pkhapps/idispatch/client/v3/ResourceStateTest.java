package net.pkhapps.idispatch.client.v3;

import net.pkhapps.idispatch.client.v3.ResourceState;
import net.pkhapps.idispatch.client.v3.ResourceStateId;
import net.pkhapps.idispatch.client.v3.test.JsonObjectTest;
import net.pkhapps.idispatch.client.v3.util.Color;
import net.pkhapps.idispatch.client.v3.util.MultilingualString;
import org.junit.Test;

import java.util.Set;

import static net.pkhapps.idispatch.client.v3.util.Locales.FINNISH;
import static net.pkhapps.idispatch.client.v3.util.Locales.SWEDISH;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * Unit test for {@link ResourceState}.
 */
@SuppressWarnings("WeakerAccess")
public class ResourceStateTest extends JsonObjectTest {

    public static ResourceState createTestResourceState() {
        return new ResourceState(new ResourceStateId(1L),
                new MultilingualString.Builder()
                        .withValue(SWEDISH, "På väg")
                        .withValue(FINNISH, "Matkalla")
                        .build(),
                new Color("#00FF00"),
                true,
                Set.of(new ResourceStateId(2L)),
                true,
                Set.of(new ResourceStateId(2L), new ResourceStateId(3L)),
                true,
                true);
    }

    @Test
    public void serializeAndDeserialize() {
        var original = createTestResourceState();
        var json = getGson().toJson(original);
        System.out.println(json);
        var deserialized = getGson().fromJson(json, ResourceState.class);
        assertThat(deserialized).isNotSameAs(original);
        assertThat(deserialized).isEqualTo(original);
    }
}
