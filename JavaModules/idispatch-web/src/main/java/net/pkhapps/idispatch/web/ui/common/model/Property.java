package net.pkhapps.idispatch.web.ui.common.model;

import com.vaadin.shared.Registration;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

import java.io.Serializable;

/**
 * Interface declaring a single property that can be observed for value changes. This is intended to be used
 * by model classes and bound to UI fields.
 */
public interface Property<T> extends Serializable {

    @Nullable
    T getValue();

    /**
     * Checks whether this property currently has a value. This is the opposite of the {@link #isEmpty()}.
     */
    boolean hasValue();

    /**
     * Checks whether this property is currently empty. This is the opposite of the {@link #hasValue()}.
     */
    default boolean isEmpty() {
        return !hasValue();
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
