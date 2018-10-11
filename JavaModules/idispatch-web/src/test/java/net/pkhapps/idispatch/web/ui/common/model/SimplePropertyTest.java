package net.pkhapps.idispatch.web.ui.common.model;

import org.junit.Test;
import org.mockito.ArgumentCaptor;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

/**
 * Unit test for {@link SimpleProperty}.
 */
public class SimplePropertyTest {

    @Test
    public void emptyConstructor_initialValueIsNullAndPropertyIsEmpty() {
        var property = new SimpleProperty<>(String.class);
        assertThat(property.isEmpty()).isTrue();
        assertThat(property.hasValue()).isFalse();
        assertThat(property.getValue()).isNull();
    }

    @Test
    public void initializingConstructor_valueIsSet() {
        var property = new SimpleProperty<>(String.class, "foobar");
        assertThat(property.isEmpty()).isFalse();
        assertThat(property.hasValue()).isTrue();
        assertThat(property.getValue()).isEqualTo("foobar");
    }

    @Test
    @SuppressWarnings("unchecked")
    public void addPropertyListenerAndFireEvent_listenerGetsInitialValue() {
        var listener = mock(PropertyListener.class);
        var event = ArgumentCaptor.forClass(PropertyChangeEvent.class);
        var property = new SimpleProperty<>(String.class, "foobar");
        property.addPropertyListenerAndFireEvent(listener);

        verify(listener).onPropertyChangeEvent(event.capture());
        assertThat(event.getValue().getProperty()).isSameAs(property);
        assertThat(event.getValue().getOldValue()).isNull();
        assertThat(event.getValue().getValue()).isEqualTo("foobar");
    }

    @Test
    @SuppressWarnings("unchecked")
    public void setValue_valueDifferentThanInitial_eventIsFired() {
        var listener = mock(PropertyListener.class);
        var event = ArgumentCaptor.forClass(PropertyChangeEvent.class);
        var property = new SimpleProperty<>(String.class, "foobar");
        property.addPropertyListener(listener);

        property.setValue("hello");
        assertThat(property.getValue()).isEqualTo("hello");

        verify(listener).onPropertyChangeEvent(event.capture());
        assertThat(event.getValue().getOldValue()).isEqualTo("foobar");
        assertThat(event.getValue().getValue()).isEqualTo("hello");
    }
}
