package net.pkhapps.idispatch.web.ui.common.model;

import com.vaadin.server.SerializableSupplier;
import org.springframework.lang.NonNull;

import java.util.Objects;

/**
 * Concrete {@link Action} implementation that wraps a lambda or method pointer and invokes it whenever the action
 * is executed.
 */
public class ActionWrapper<T> extends AbstractAction<T> {

    private final SerializableSupplier<T> action;

    /**
     * Creates a new action wrapper.
     *
     * @param action the action to execute.
     */
    public ActionWrapper(@NonNull SerializableSupplier<T> action) {
        this.action = Objects.requireNonNull(action, "action must not be null");
    }

    @Override
    protected T doExecute() {
        return action.get();
    }
}
