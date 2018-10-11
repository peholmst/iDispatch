package net.pkhapps.idispatch.client.v3.geo;

import net.pkhapps.idispatch.client.v3.test.JsonObjectTest;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Unit test for {@link GeographicLocation}.
 */
public class GeographicLocationTest extends JsonObjectTest {

    @Test
    public void latitudeIsY() {
        var location = new GeographicLocation(CoordinateReferenceSystem.WGS84, 60.306718664, 22.300982687);
        assertThat(location.latitude()).isEqualTo(location.y());
    }

    @Test
    public void longitudeIsX() {
        var location = new GeographicLocation(CoordinateReferenceSystem.ETRS_GK27, 6697501.995, 27240374.155);
        assertThat(location.longitude()).isEqualTo(location.x());
    }

    @Test
    public void serializeAndDeserialize() {
        var original = new GeographicLocation(CoordinateReferenceSystem.ETRS_TM35FIN, 6694823, 240478);
        var json = getGson().toJson(original);
        System.out.println(json);
        var deserialized = getGson().fromJson(json, GeographicLocation.class);
        assertThat(deserialized).isNotSameAs(original);
        assertThat(deserialized).isEqualTo(original);
    }
}
