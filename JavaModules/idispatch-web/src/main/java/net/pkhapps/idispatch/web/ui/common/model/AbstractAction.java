package net.pkhapps.idispatch.web.ui.common.model;

import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

/**
 * Base class for {@link Action} implementations.
 */
public abstract class AbstractAction<T> implements Action<T> {

    private final SimpleProperty<Boolean> canPerform = new SimpleProperty<>(Boolean.class, true);

    @Override
    @NonNull
    public Property<Boolean> canPerform() {
        return canPerform;
    }

    @Override
    @Nullable
    public T perform() {
        if (canPerform.getValue()) {
            return doPerform();
        }
        return null;
    }

    /**
     * Enables this action. Clients will be able to perform it after this.
     */
    protected void enable() {
        canPerform.setValue(true);
    }

    /**
     * Disables this action. Clients will not be able to perform it until it is {@link #enable() enabled} again.
     */
    protected void disable() {
        canPerform.setValue(false);
    }

    /**
     * Performs the action if it is enabled. This method is never called if {@link #canPerform()} is false.
     *
     * @return any result to pass to the performing client.
     */
    @Nullable
    protected abstract T doPerform();
}
