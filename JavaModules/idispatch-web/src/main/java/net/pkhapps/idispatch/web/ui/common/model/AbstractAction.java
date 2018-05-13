package net.pkhapps.idispatch.web.ui.common.model;

import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

/**
 * Base class for {@link Action} implementations.
 */
public abstract class AbstractAction<T> implements Action<T> {

    private final SimpleProperty<Boolean> isExecutable = new SimpleProperty<>(Boolean.class, true);

    @Override
    @NonNull
    public Property<Boolean> isExecutable() {
        return isExecutable;
    }

    @Override
    @Nullable
    public T execute() {
        if (isExecutable.getValue()) {
            return doPerform();
        }
        return null;
    }

    /**
     * Sets the value of the {@link #isExecutable() writable} flag. By default this flag is true, i.e. the action
     * is executable.
     */
    protected void setExecutable(boolean executable) {
        isExecutable.setValue(executable);
    }

    /**
     * Executes the action if it is executable. This method is never called if {@link #isExecutable()} is false.
     *
     * @return any result to pass to the performing client.
     */
    @Nullable
    protected abstract T doPerform();
}
