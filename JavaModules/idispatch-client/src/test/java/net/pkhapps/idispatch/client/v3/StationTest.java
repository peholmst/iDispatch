package net.pkhapps.idispatch.client.v3;

import net.pkhapps.idispatch.client.v3.Station;
import net.pkhapps.idispatch.client.v3.StationId;
import net.pkhapps.idispatch.client.v3.geo.CoordinateReferenceSystem;
import net.pkhapps.idispatch.client.v3.geo.GeographicLocation;
import net.pkhapps.idispatch.client.v3.test.JsonObjectTest;
import net.pkhapps.idispatch.client.v3.util.MultilingualString;
import org.junit.Test;

import static net.pkhapps.idispatch.client.v3.util.Locales.FINNISH;
import static net.pkhapps.idispatch.client.v3.util.Locales.SWEDISH;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * Unit test for {@link Station}.
 */
@SuppressWarnings("WeakerAccess")
public class StationTest extends JsonObjectTest {

    public static Station createTestStation() {
        return new Station(new StationId(1L),
                new MultilingualString.Builder()
                        .withValue(SWEDISH, "Rondellens brandstation")
                        .withValue(FINNISH, "Liikenneympyrän paloasema")
                        .build(),
                new GeographicLocation(CoordinateReferenceSystem.ETRS_TM35FIN, 6694823, 240478),
                true);
    }

    @Test
    public void serializeAndDeserialize() {
        var original = createTestStation();
        var json = getGson().toJson(original);
        System.out.println(json);
        var deserialized = getGson().fromJson(json, Station.class);
        assertThat(deserialized).isNotSameAs(original);
        assertThat(deserialized).isEqualTo(original);
    }
}
