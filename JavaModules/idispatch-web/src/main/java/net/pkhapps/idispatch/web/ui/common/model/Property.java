package net.pkhapps.idispatch.web.ui.common.model;

import com.vaadin.shared.Registration;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

import java.io.Serializable;
import java.util.Objects;

/**
 * Interface declaring a single property that can be observed for value changes. This is intended to be used
 * by model classes and bound to UI fields. It is very similar to {@link com.vaadin.data.HasValue} since properties
 * will often be bound to fields that implement that particular interface.
 */
public interface Property<T> extends Serializable {

    /**
     * Returns the type of this property.
     */
    @NonNull
    Class<T> getType();

    /**
     * Returns the value of this property.
     */
    @Nullable
    T getValue();

    /**
     * Checks whether this property currently has a non-empty, non-null value. This is the opposite of the {@link #isEmpty()}.
     *
     * @see #getEmptyValue()
     */
    default boolean hasValue() {
        var value = getValue();
        return value != null && !Objects.equals(value, getEmptyValue());
    }

    /**
     * Checks whether this property is currently empty. This is the opposite of the {@link #hasValue()}.
     */
    default boolean isEmpty() {
        return !hasValue();
    }

    /**
     * Returns the property value that is considered an empty value. By default this is null.
     *
     * @see #hasValue()
     * @see #isEmpty()
     */
    @Nullable
    default T getEmptyValue() {
        return null;
    }

    /**
     * Registers the listener to be notified when the property value changes.
     *
     * @param listener the listener to register.
     * @return a registration handle that can be used to unregister the listener.
     * @see #addPropertyListenerAndFireEvent(PropertyListener)
     */
    @NonNull
    Registration addPropertyListener(@NonNull PropertyListener<T> listener);

    /**
     * Registers the listener and then immediately invokes it using a {@link PropertyChangeEvent} to allow the listener
     * to update its internal state.
     *
     * @param listener the listener to register and invoke.
     * @return a registration handle that can be used to unregister the listener.
     * @see #addPropertyListener(PropertyListener)
     */
    @NonNull
    default Registration addPropertyListenerAndFireEvent(@NonNull PropertyListener<T> listener) {
        var registration = addPropertyListener(listener);
        listener.onPropertyChangeEvent(new PropertyChangeEvent<>(this, null, getValue()));
        return registration;
    }
}
