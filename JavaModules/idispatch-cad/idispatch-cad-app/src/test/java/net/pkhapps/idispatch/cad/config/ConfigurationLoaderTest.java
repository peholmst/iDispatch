package net.pkhapps.idispatch.cad.config;

import lombok.AllArgsConstructor;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Unit test for {@link ConfigurationLoader} (well, you could argue that this is an integration test).
 */
public class ConfigurationLoaderTest {

    @Test
    public void load_noActiveTestProfile() {
        var config = new DefaultConfiguration();
        var loader = new ConfigurationLoader(config, "test");
        Profile.deactivateProfile("test-profile");
        loader.load();

        assertThat(config.attribute(TestAttributes.STRING_PROPERTY, String.class)).isEqualTo("Hello World");
        assertThat(config.attribute(TestAttributes.INT_PROPERTY, Integer.class)).isEqualTo(123);
        assertThat(config.attribute(TestAttributes.BOOLEAN_PROPERTY, Boolean.class)).isTrue();
    }

    @Test
    public void load_activeTestProfile() {
        var config = new DefaultConfiguration();
        var loader = new ConfigurationLoader(config, "test");
        Profile.activateProfile("test-profile");
        loader.load();

        assertThat(config.attribute(TestAttributes.STRING_PROPERTY, String.class)).isEqualTo("Overridden");
        assertThat(config.attribute(TestAttributes.INT_PROPERTY, Integer.class)).isEqualTo(123);
        assertThat(config.attribute(TestAttributes.BOOLEAN_PROPERTY, Boolean.class)).isTrue();
    }

    @AllArgsConstructor
    enum TestAttributes implements Configuration.AttributeName {
        STRING_PROPERTY("test.string-property"),
        INT_PROPERTY("test.int-property"),
        BOOLEAN_PROPERTY("test.boolean-property");

        private final String attributeName;

        @Override
        public String toString() {
            return attributeName;
        }
    }

}
