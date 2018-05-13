package net.pkhapps.idispatch.web.ui.common.model;

import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

import java.io.Serializable;

/**
 * Interface declaring an action that can be performed. This is intended to be used by model classes or controller
 * classes and bound to UI action elements such as buttons or menu items.
 */
public interface Action<T> extends Serializable {

    /**
     * A property indicating whether this action can currently be performed or not.
     */
    @NonNull
    Property<Boolean> canPerform();

    /**
     * Performs the action and returns any result the action may produce. If the action is currently not
     * {@link #canPerform() performable}, nothing happens and {@code null} is returned.
     */
    @Nullable
    T perform();
}
