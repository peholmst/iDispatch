package net.pkhapps.idispatch.client.v3.util;

import com.google.gson.Gson;
import org.junit.Before;
import org.junit.Test;

import java.util.Locale;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Unit test for {@link MultilingualString}.
 */
public class MultilingualStringTest {

    private static final Locale FINNISH = new Locale("fi");
    private static final Locale SWEDISH = new Locale("sv");

    private Gson gson;

    @Before
    public void setUp() {
        gson = new Gson();
    }

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
        var json = gson.toJson(original);
        System.out.println(json);
        var deserialized = gson.fromJson(json, MultilingualString.class);
        assertThat(deserialized).isNotSameAs(original);
        assertThat(deserialized).isEqualTo(original);
    }
}
