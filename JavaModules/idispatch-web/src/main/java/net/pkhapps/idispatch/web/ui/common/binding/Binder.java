package net.pkhapps.idispatch.web.ui.common.binding;

import com.vaadin.ui.AbstractTextField;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;
import org.springframework.lang.NonNull;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * Helper class for creating and managing a set of {@link AbstractFieldBinding bindings}. Clients typically create one
 * binder for each view and uses it to bind the UI fields to the model properties. When the view is disposed of, the
 * {@link #removeAllBindings()} method should be called.
 */
public class Binder implements Serializable {

    private Set<Binding> bindings = new HashSet<>();

    /**
     * Creates a new binding for the given text field.
     */
    @NonNull
    public TextFieldBinding<String> forField(@NonNull AbstractTextField textField) {
        return registerBinding(TextFieldBinding.forField(textField));
    }

    /**
     * Creates a new binding for the given combo box.
     */
    @NonNull
    public <T> ComboBoxBinding<T> forField(@NonNull ComboBox<T> comboBox) {
        return registerBinding(ComboBoxBinding.forField(comboBox));
    }

    /**
     * TODO Document me!
     *
     * @param button
     * @param <T>
     * @return
     */
    @NonNull
    public ButtonBinding forButton(@NonNull Button button) {
        throw new UnsupportedOperationException("not implemented yet"); // TODO Implement me!
    }

    /**
     * Registers the given binding with this binder. The binding will be unbound whenever {@link #removeAllBindings()}
     * is called.
     *
     * @param binding the binding to register.
     * @return the same {@code binding} instance to allow for method chaining.
     */
    @NonNull
    public <B extends Binding> B registerBinding(@NonNull B binding) {
        bindings.add(binding);
        return binding;
    }

    /**
     * {@link Binding#unbind() Unbinds} and removes all binders that have been registered with this binder.
     * Clients should remember to invoke this method when they no longer need the bindings to avoid memory leaks.
     */
    public void removeAllBindings() {
        bindings.forEach(Binding::unbind);
        bindings.clear();
    }
}
