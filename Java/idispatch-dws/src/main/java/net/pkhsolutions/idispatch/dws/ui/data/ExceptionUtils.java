package net.pkhsolutions.idispatch.dws.ui.data;

import java.lang.reflect.InvocationTargetException;

/**
 *
 * @author Petter Holmström
 */
public final class ExceptionUtils {

    private ExceptionUtils() {
    }

    public static RuntimeException unwrap(InvocationTargetException ex) {
        if (ex.getTargetException() instanceof RuntimeException) {
            return (RuntimeException) ex.getTargetException();
        } else {
            return new RuntimeException(ex);
        }
    }
}
