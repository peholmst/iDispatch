package net.pkhapps.idispatch.web.ui.common.model;

import org.springframework.lang.NonNull;

import java.io.Serializable;

/**
 * Listener interface for objects that want to get notified when the value of a {@link Property} changes.
 */
@FunctionalInterface
public interface PropertyListener<T> extends Serializable {

    /**
     * Invoked when the value of the property has changed.
     */
    void onPropertyChangeEvent(@NonNull PropertyChangeEvent<T> event);
}
