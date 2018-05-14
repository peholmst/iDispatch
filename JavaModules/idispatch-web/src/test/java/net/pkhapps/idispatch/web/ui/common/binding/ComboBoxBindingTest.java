package net.pkhapps.idispatch.web.ui.common.binding;

import com.vaadin.ui.ComboBox;
import net.pkhapps.idispatch.web.ui.common.model.SimpleProperty;
import net.pkhapps.idispatch.web.ui.common.model.SimpleWritableProperty;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Unit test for {@link ComboBoxBinding}.
 */
public class ComboBoxBindingTest {

    @Test
    public void unidirectionalBinding_propertyIsEmpty_fieldAlsoBecomesEmpty() {
        var field = createComboBox();
        var property = new SimpleProperty<>(String.class);
        ComboBoxBinding.forField(field).bind(property);

        assertThat(field.isReadOnly()).isTrue();
        assertThat(field.isEmpty()).isTrue();
    }

    @Test
    public void unidirectionalBinding_propertyContainsAValue_fieldAlsoGetsTheValue() {
        var field = createComboBox();
        var property = new SimpleProperty<>(String.class, "hello");
        ComboBoxBinding.forField(field).bind(property);

        assertThat(field.isReadOnly()).isTrue();
        assertThat(field.getValue()).isEqualTo("hello");
    }

    @Test
    public void bidirectionalBinding_fieldIsUpdated_propertyAlsoBecomesUpdated() {
        var field = createComboBox();
        var property = new SimpleWritableProperty<>(String.class);
        ComboBoxBinding.forField(field).bind(property);

        field.setValue("world");

        assertThat(property.getValue()).isEqualTo("world");
    }

    @Test
    public void bidirectionalBinding_propertyWritableFlagChanges_fieldReadOnlyFlagAlsoChanges() {
        var field = createComboBox();
        var property = new SimpleWritableProperty<>(String.class);
        ComboBoxBinding.forField(field).bind(property);

        assertThat(property.isWritable().getValue()).isTrue();
        assertThat(field.isReadOnly()).isFalse();

        property.setWritable(false);

        assertThat(property.isWritable().getValue()).isFalse();
        assertThat(field.isReadOnly()).isTrue();
    }

    private ComboBox<String> createComboBox() {
        var comboBox = new ComboBox<String>();
        comboBox.setItems("hello", "world");
        return comboBox;
    }
}
