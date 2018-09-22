package net.pkhapps.idispatch.client.v3.type;

import net.pkhapps.idispatch.client.v3.test.JsonObjectTest;
import net.pkhapps.idispatch.client.v3.util.MultilingualString;
import org.junit.Test;

import static net.pkhapps.idispatch.client.v3.util.Locales.FINNISH;
import static net.pkhapps.idispatch.client.v3.util.Locales.SWEDISH;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * Unit test for {@link Municipality}.
 */
@SuppressWarnings("WeakerAccess")
public class MunicipalityTest extends JsonObjectTest {

    public static Municipality createTestMunicipality() {
        return new Municipality(new MunicipalityId(1L),
                new MultilingualString.Builder()
                        .withValue(SWEDISH, "Testkommun")
                        .withValue(FINNISH, "Testikunta")
                        .build(),
                true);
    }

    @Test
    public void serializeAndDeserialize() {
        var original = createTestMunicipality();
        var json = getGson().toJson(original);
        System.out.println(json);
        var deserialized = getGson().fromJson(json, Municipality.class);
        assertThat(deserialized).isNotSameAs(original);
        assertThat(deserialized).isEqualTo(original);
    }
}
