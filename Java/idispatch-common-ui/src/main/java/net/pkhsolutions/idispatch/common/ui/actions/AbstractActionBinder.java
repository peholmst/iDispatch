package net.pkhsolutions.idispatch.common.ui.actions;

import java.io.Serializable;
import java.lang.reflect.Method;

/**
 *
 * @author peholmst
 */
public abstract class AbstractActionBinder implements Serializable {

    protected final Serializable target;

    public AbstractActionBinder(Serializable target) {
        this.target = target;
    }

    protected Method findMethodForAction(String action) {
        for (Method m : target.getClass().getDeclaredMethods()) {
            if (m.isAnnotationPresent(ActionMethod.class) && m.getAnnotation(ActionMethod.class).action().equals(action)) {
                return m;
            }
        }
        throw new IllegalStateException("No action method found for action " + action);
    }
}
