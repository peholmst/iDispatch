package net.pkhapps.idispatch.web.ui.common.binding;

import com.vaadin.data.converter.StringToIntegerConverter;
import com.vaadin.server.SerializableConsumer;
import com.vaadin.ui.TextField;
import net.pkhapps.idispatch.web.ui.common.model.SimpleProperty;
import net.pkhapps.idispatch.web.ui.common.model.SimpleWritableProperty;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

/**
 * Unit test for {@link TextFieldBinding}.
 */
public class TextFieldBindingTest {

    @Test
    public void unidirectionalBinding_propertyIsEmpty_fieldAlsoBecomesEmpty() {
        var field = new TextField();
        var property = new SimpleProperty<>(String.class);
        TextFieldBinding.forField(field).bind(property);

        assertThat(field.isReadOnly()).isTrue();
        assertThat(field.isEmpty()).isTrue();
    }

    @Test
    public void unidirectionalBinding_propertyContainsAValue_fieldAlsoGetsTheValue() {
        var field = new TextField();
        var property = new SimpleProperty<>(String.class, "foobar");
        TextFieldBinding.forField(field).bind(property);

        assertThat(field.isReadOnly()).isTrue();
        assertThat(field.getValue()).isEqualTo("foobar");
    }

    @Test
    public void unidirectionalBindingWithConverter_propertyIsEmpty_fieldAlsoBecomesEmpty() {
        var field = new TextField();
        var property = new SimpleProperty<>(Integer.class);
        TextFieldBinding.forField(field).withConverter(new StringToIntegerConverter("error message")).bind(property);

        assertThat(field.isReadOnly()).isTrue();
        assertThat(field.isEmpty()).isTrue();
    }

    @Test
    public void unidirectionalBindingWithConverter_propertyHasAValue_fieldAlsoGetsTheValue() {
        var field = new TextField();
        var property = new SimpleProperty<>(Integer.class, 123);
        TextFieldBinding.forField(field).withConverter(new StringToIntegerConverter("error message")).bind(property);

        assertThat(field.isReadOnly()).isTrue();
        assertThat(field.getValue()).isEqualTo("123");
    }

    @Test
    public void bidirectionalBinding_fieldIsUpdatedByUser_propertyAlsoBecomesUpdated() {
        var field = new TestTextField();
        var property = new SimpleWritableProperty<>(String.class);
        TextFieldBinding.forField(field).bind(property);

        field.simulateUserInput("foobar");

        assertThat(property.getValue()).isEqualTo("foobar");
    }

    @Test
    public void bidirectionalBinding_fieldIsUpdatedProgrammatically_propertyAlsoBecomesUpdated() {
        var field = new TextField();
        var property = new SimpleWritableProperty<>(String.class);
        TextFieldBinding.forField(field).bind(property);

        field.setValue("foobar");

        assertThat(property.getValue()).isEqualTo("foobar");
    }

    @Test
    public void bidirectionalBinding_propertyWritableFlagChanges_fieldReadOnlyFlagAlsoChanges() {
        var field = new TextField();
        var property = new SimpleWritableProperty<>(String.class);
        TextFieldBinding.forField(field).bind(property);

        assertThat(property.isWritable().getValue()).isTrue();
        assertThat(field.isReadOnly()).isFalse();

        property.setWritable(false);

        assertThat(property.isWritable().getValue()).isFalse();
        assertThat(field.isReadOnly()).isTrue();
    }

    @Test
    public void bidirectionalBindingWithConverter_fieldIsUpdated_propertyAlsoBecomesUpdated() {
        var field = new TextField();
        var property = new SimpleWritableProperty<>(Integer.class);
        TextFieldBinding.forField(field).withConverter(new StringToIntegerConverter("error message")).bind(property);

        field.setValue("123");

        assertThat(property.getValue()).isEqualTo(123);
    }

    @Test
    public void bidirectionalBindingWithConverter_conversionFails_propertyRemainsUnchanged() {
        var field = new TextField();
        var property = new SimpleWritableProperty<>(Integer.class);
        TextFieldBinding.forField(field).withConverter(new StringToIntegerConverter("error message")).bind(property);

        field.setValue("rock and roll");

        assertThat(property.getValue()).isNull();
    }

    @Test
    @SuppressWarnings("unchecked")
    public void bidirectionalBindingWithConverter_conversionFailsWithHandler_propertyRemainsUnchangedAndHandlerIsInvoked() {
        var handler = mock(SerializableConsumer.class);
        var field = new TextField();
        var property = new SimpleWritableProperty<>(Integer.class);
        TextFieldBinding.forField(field).withConverter(new StringToIntegerConverter("error message")).bind(property, handler);

        field.setValue("rock and roll");

        assertThat(property.getValue()).isNull();
        var errorMessage = ArgumentCaptor.forClass(String.class);
        verify(handler).accept(errorMessage.capture());
        assertThat(errorMessage.getValue()).isEqualTo("error message");
    }

    static class TestTextField extends TextField {
        void simulateUserInput(String input) {
            setValue(input, true);
        }
    }
}
