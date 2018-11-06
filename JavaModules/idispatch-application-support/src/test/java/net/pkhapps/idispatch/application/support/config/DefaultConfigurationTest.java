package net.pkhapps.idispatch.application.support.config;

import org.junit.Test;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Unit test for {@link DefaultConfiguration}.
 */
public class DefaultConfigurationTest {

    @Test
    public void setOne_attributeDoesNotExist_attributeIsCreated() {
        var config = new DefaultConfiguration();
        var listener = new AtomicReference<Configuration.AttributeChangeEvent>();
        config.registerListener(listener::set);
        config.setOne("hello", "world");

        assertThat(config.attribute("hello", String.class)).isEqualTo("world");
        assertThat(config.attributeNames()).containsOnly("hello");
        assertThat(listener.get().configuration()).isSameAs(config);
        assertThat(listener.get().changedAttributeNames()).containsOnly("hello");
    }

    @Test
    public void setOne_attributeExistsButHasDifferentValue_attributeIsChanged() {
        var config = new DefaultConfiguration();
        config.setOne("hello", "old");
        assertThat(config.attribute("hello", String.class)).isEqualTo("old");

        var listener = new AtomicReference<Configuration.AttributeChangeEvent>();
        config.registerListener(listener::set);
        config.setOne("hello", "world");

        assertThat(config.attribute("hello", String.class)).isEqualTo("world");
        assertThat(listener.get().configuration()).isSameAs(config);
        assertThat(listener.get().changedAttributeNames()).containsOnly("hello");
    }

    @Test
    public void setOne_attributeExistsAndHasSameValue_nothingHappens() {
        var config = new DefaultConfiguration();
        config.setOne("hello", "world");

        var listener = new AtomicReference<Configuration.AttributeChangeEvent>();
        config.registerListener(listener::set);
        config.setOne("hello", "world");

        assertThat(listener.get()).isNull();
    }

    @Test
    public void setMultiple_attributesDoNotExist_attributesAreCreated() {
        var config = new DefaultConfiguration();
        var listener = new AtomicReference<Configuration.AttributeChangeEvent>();
        config.registerListener(listener::set);
        config.setMultiple(Map.of("prop1", "val1", "prop2", "val2"));

        assertThat(config.attribute("prop1", String.class)).isEqualTo("val1");
        assertThat(config.attribute("prop2", String.class)).isEqualTo("val2");
        assertThat(config.attributeNames()).containsOnly("prop1", "prop2");
        assertThat(listener.get().configuration()).isSameAs(config);
        assertThat(listener.get().changedAttributeNames()).containsOnly("prop1", "prop2");
    }

    @Test
    public void setMultiple_attributesExistButHaveDifferentValues_attributesAreChanged() {
        var config = new DefaultConfiguration();
        config.setOne("prop1", "val1");
        config.setOne("prop2", "val2");
        var listener = new AtomicReference<Configuration.AttributeChangeEvent>();
        config.registerListener(listener::set);
        config.setMultiple(Map.of("prop1", "change1", "prop2", "change2"));

        assertThat(config.attribute("prop1", String.class)).isEqualTo("change1");
        assertThat(config.attribute("prop2", String.class)).isEqualTo("change2");
        assertThat(config.attributeNames()).containsOnly("prop1", "prop2");
        assertThat(listener.get().configuration()).isSameAs(config);
        assertThat(listener.get().changedAttributeNames()).containsOnly("prop1", "prop2");
    }

    @Test
    public void setMultiple_oneAttributeExistsAndHasSameValue_theOtherAttributeIsChanged() {
        var config = new DefaultConfiguration();
        config.setOne("prop1", "val1");
        config.setOne("prop2", "val2");
        var listener = new AtomicReference<Configuration.AttributeChangeEvent>();
        config.registerListener(listener::set);
        config.setMultiple(Map.of("prop1", "val1", "prop2", "change2"));

        assertThat(config.attribute("prop1", String.class)).isEqualTo("val1");
        assertThat(config.attribute("prop2", String.class)).isEqualTo("change2");
        assertThat(config.attributeNames()).containsOnly("prop1", "prop2");
        assertThat(listener.get().configuration()).isSameAs(config);
        assertThat(listener.get().changedAttributeNames()).containsOnly("prop2");
    }

    @Test
    public void fireEvent_listenerThrowsException_exceptionIsIgnored() {
        var config = new DefaultConfiguration();
        config.registerListener(event -> {
            throw new RuntimeException("NO!");
        });
        config.setOne("foo", "bar");
    }

    @Test
    public void attribute_integerString_convertedToCorrectType() {
        var config = new DefaultConfiguration();
        config.setOne("foo", 123);
        assertThat(config.attribute("foo", Integer.class)).isEqualTo(123);
    }

    @Test
    public void attribute_booleanString_convertedToCorrectType() {
        var config = new DefaultConfiguration();
        config.setOne("foo", false);
        assertThat(config.attribute("foo", Boolean.class)).isFalse();
    }

    @Test(expected = IllegalArgumentException.class)
    public void attribute_valueCannotBeConverted() {
        var config = new DefaultConfiguration();
        config.setOne("foo", "this is not an integer");
        config.attribute("foo", Integer.class);
    }

    @Test(expected = IllegalArgumentException.class)
    public void attribute_valueCannotBeConvertedBecauseOfMissingFactoryMethod() {
        var config = new DefaultConfiguration();
        config.setOne("foo", UUID.randomUUID().toString());
        config.attribute("foo", UUID.class);
    }

    @Test(expected = IllegalArgumentException.class)
    public void attribute_attributeDoesNotExist_exceptionThrown() {
        var config = new DefaultConfiguration();
        config.attribute("nonexistent", String.class);
    }

    @Test
    public void attribute_attributeDoesNotExist_defaultValueIsReturned() {
        var config = new DefaultConfiguration();
        assertThat(config.attribute("foo", String.class, "bar")).isEqualTo("bar");
    }
}
