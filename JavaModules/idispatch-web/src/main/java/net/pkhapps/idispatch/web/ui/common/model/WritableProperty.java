package net.pkhapps.idispatch.web.ui.common.model;

import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

/**
 * Extension of {@link Property} that allows clients to set the property value.
 */
public interface WritableProperty<T> extends Property<T> {

    /**
     * A property indicating whether this property can currently be written to or not.
     */
    @NonNull
    Property<Boolean> isWritable();

    /**
     * Sets the value of this property.
     *
     * @param value the value to set.
     * @throws IllegalStateException if the property is currently not {@link #isWritable() writable}.
     */
    void setValue(@Nullable T value);
}
