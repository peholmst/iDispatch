package net.pkhapps.idispatch.web.ui.common.model;

import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

/**
 * Simple implementation of {@link WritableProperty} that should be sufficient in most cases.
 * The property should always be exposed to clients using its interface, not the implementation class.
 */
public class SimpleWritableProperty<T> extends SimpleProperty<T> implements WritableProperty<T> {

    private final SimpleProperty<Boolean> isWritable = new SimpleProperty<>(true);

    /**
     * Creates a new empty property.
     */
    public SimpleWritableProperty() {
        super();
    }

    /**
     * Creates a new property with the given initial value.
     *
     * @param initialValue the initial property value.
     */
    public SimpleWritableProperty(@Nullable T initialValue) {
        super(initialValue);
    }

    @Override
    @NonNull
    public Property<Boolean> isWritable() {
        return isWritable;
    }

    /**
     * Sets the value of the {@link #isWritable() writable} flag. By default this flag is true, i.e. the property
     * is writable.
     */
    public void setWritable(boolean writable) {
        isWritable.setValue(writable);
    }

    @Override
    public void setValue(@Nullable T value) {
        if (!isWritable.getValue()) {
            throw new IllegalStateException("Property is not writable");
        }
        super.setValue(value);
    }

    /**
     * Sets the value of this property regardless of whether it is {@link #isWritable() writable} or not.
     *
     * @param value the value to set.
     */
    public void forceSetValue(@Nullable T value) {
        super.setValue(value);
    }
}
