package net.pkhapps.idispatch.client.v3.util;

import org.junit.Test;

import static org.assertj.core.api.Java6Assertions.assertThat;

/**
 * Unit test for {@link Color}.
 */
public class ColorTest extends JsonObjectTest {

    @Test
    public void constructWithHash() {
        var color = new Color("#FF0033");
        assertThat(color.toHexRGBString()).isEqualTo("#FF0033");
    }

    @Test
    public void constructWithoutHash() {
        var color = new Color("FF0033");
        assertThat(color.toHexRGBString()).isEqualTo("#FF0033");
    }

    @Test
    public void serializeAndDeserialize() {
        var original = new Color("FFCC01");
        var json = getGson().toJson(original);
        System.out.println(json);
        var deserialized = getGson().fromJson(json, Color.class);
        assertThat(deserialized).isNotSameAs(original);
        assertThat(deserialized).isEqualTo(original);
    }
}