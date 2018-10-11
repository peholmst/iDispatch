package net.pkhapps.idispatch.web.ui.common.model;

import org.springframework.lang.NonNull;

/**
 * Action wrapper for actions that don't return a result.
 */
public class VoidActionWrapper extends ActionWrapper<Void> {

    /**
     * Creates a new action wrapper.
     *
     * @param action the action to execute.
     */
    public VoidActionWrapper(@NonNull Runnable action) {
        super(() -> { // No nice way of adding a null check on action, just assume it is non-null
            action.run();
            return null;
        });
    }
}
