package net.pkhapps.idispatch.web.ui.common.model;

import com.vaadin.shared.Registration;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

/**
 * Simple implementation of {@link Property} that should be sufficient in most cases.
 * The property should always be exposed to clients using its interface, not the implementation class.
 */
public class SimpleProperty<T> implements Property<T> {

    private final List<PropertyListener<T>> listeners = new LinkedList<>();
    private T value;

    /**
     * Creates a new empty property.
     */
    public SimpleProperty() {
        this(null);
    }

    /**
     * Creates a new property with the given initial value.
     *
     * @param initialValue the initial property value.
     */
    public SimpleProperty(@Nullable T initialValue) {
        value = initialValue;
    }

    @Override
    @Nullable
    public T getValue() {
        return value;
    }

    /**
     * Sets the value of this property, firing a {@link PropertyChangeEvent} to all registered listeners.
     *
     * @param value the value to set.
     */
    public void setValue(@Nullable T value) {
        if (!Objects.equals(this.value, value)) {
            var old = this.value;
            this.value = value;
            var changeEvent = new PropertyChangeEvent<>(this, old, value);
            new ArrayList<>(listeners).forEach(listener -> listener.onPropertyChangeEvent(changeEvent));
        }
    }

    @Override
    public boolean hasValue() {
        return value != null;
    }

    @Override
    @NonNull
    public Registration addPropertyListener(@NonNull PropertyListener<T> listener) {
        Objects.requireNonNull(listener, "listener must not be null");
        listeners.add(listener);
        return () -> listeners.remove(listener);
    }
}
