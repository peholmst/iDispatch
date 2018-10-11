package net.pkhapps.idispatch.web.ui.common.model;

import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

import java.util.Objects;

/**
 * Event fired by a {@link Property} when its value changes.
 */
public class PropertyChangeEvent<T> {

    private final Property<T> property;
    private final T oldValue;
    private final T value;

    /**
     * Creates a new {@code PropertyChangeEvent}.
     *
     * @param property the property that has changed.
     * @param oldValue the old value.
     * @param value    the new value.
     */
    public PropertyChangeEvent(@NonNull Property<T> property, @Nullable T oldValue, @Nullable T value) {
        this.property = Objects.requireNonNull(property, "property must not be null");
        this.oldValue = oldValue;
        this.value = value;
    }

    /**
     * Returns the property whose value has changed.
     */
    @NonNull
    public Property<T> getProperty() {
        return property;
    }

    /**
     * Returns the old property value.
     */
    @Nullable
    public T getOldValue() {
        return oldValue;
    }

    /**
     * Returns the new (current) property value.
     */
    @Nullable
    public T getValue() {
        return value;
    }
}
