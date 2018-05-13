package net.pkhapps.idispatch.web.ui.common.binding;

import com.vaadin.ui.ComboBox;
import org.springframework.lang.NonNull;

/**
 * Binder that binds a {@link ComboBox} to a property.
 */
public class ComboBoxBinding<MODEL> extends AbstractBinding<MODEL, MODEL> {

    private ComboBoxBinding(@NonNull ComboBox<MODEL> field) {
        super(field, new SelfConverter<>());
    }

    /**
     * Creates a new binding for the given combo box.
     */
    @NonNull
    public static <T> ComboBoxBinding<T> forField(@NonNull ComboBox<T> comboBox) {
        return new ComboBoxBinding<>(comboBox);
    }
}
