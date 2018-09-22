package net.pkhapps.idispatch.client.v3.util;

import net.pkhapps.idispatch.client.v3.test.JsonObjectTest;
import org.junit.Test;

import static net.pkhapps.idispatch.client.v3.util.Locales.FINNISH;
import static net.pkhapps.idispatch.client.v3.util.Locales.SWEDISH;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * Unit test for {@link MultilingualString}.
 */
public class MultilingualStringTest extends JsonObjectTest {

    @Test
    public void value_localeExists() {
        var string = new MultilingualString(FINNISH, "Terve");
        assertThat(string.hasValue(FINNISH)).isTrue();
        assertThat(string.value(FINNISH)).isEqualTo("Terve");
    }

    @Test
    public void value_localeDoesNotExist() {
        var string = new MultilingualString(FINNISH, "Terve");
        assertThat(string.hasValue(SWEDISH)).isFalse();
        assertThat(string.value(SWEDISH)).isEmpty();
    }

    @Test
    public void withValue() {
        var string = new MultilingualString(FINNISH, "Terve");
        var derived = string.withValue(SWEDISH, "Hej");
        assertThat(string).isNotSameAs(derived);
        assertThat(string).isNotEqualTo(derived);
        assertThat(string.hasValue(SWEDISH)).isFalse();
        assertThat(derived.hasValue(SWEDISH)).isTrue();
    }

    @Test
    public void withoutValue() {
        var string = new MultilingualString.Builder().withValue(FINNISH, "Terve").withValue(SWEDISH, "Hej").build();
        var derived = string.withoutValue(FINNISH);
        assertThat(string).isNotSameAs(derived);
        assertThat(string).isNotEqualTo(derived);
        assertThat(string.hasValue(FINNISH)).isTrue();
        assertThat(derived.hasValue(FINNISH)).isFalse();
    }

    @Test
    public void serializeAndDeserialize() {
        var original = new MultilingualString.Builder().withValue(FINNISH, "Terve").withValue(SWEDISH, "Hej").build();
        var json = getGson().toJson(original);
        System.out.println(json);
        var deserialized = getGson().fromJson(json, MultilingualString.class);
        assertThat(deserialized).isNotSameAs(original);
        assertThat(deserialized).isEqualTo(original);
    }
}
