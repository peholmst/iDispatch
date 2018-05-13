package net.pkhapps.idispatch.web.ui.common.model;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Unit test for {@link SimpleWritableProperty}.
 */
public class SimpleWritablePropertyTest {

    @Test
    public void setValue_canWrite_valueIsSet() {
        var property = new SimpleWritableProperty<>();
        property.setValue("foobar");

        assertThat(property.isWritable().getValue()).isTrue();
        assertThat(property.getValue()).isEqualTo("foobar");
    }

    @Test(expected = IllegalStateException.class)
    public void setValue_cannotWrite_exceptionIsThrown() {
        var property = new SimpleWritableProperty<>();
        property.setWritable(false);
        property.setValue("foobar");
    }

    @Test
    public void forceSetValue_cannotWrite_valueIsSetAnyway() {
        var property = new SimpleWritableProperty<>();
        property.setWritable(false);
        property.forceSetValue("foobar");

        assertThat(property.isWritable().getValue()).isFalse();
        assertThat(property.getValue()).isEqualTo("foobar");
    }
}
