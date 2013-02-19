package net.pkhsolutions.idispatch.ejb.masterdata;

import javax.ejb.ApplicationException;

/**
 *
 * @author peholmst
 */
@ApplicationException
public class ConcurrentModificationException extends Exception {

    public ConcurrentModificationException() {
    }

    public ConcurrentModificationException(String message) {
        super(message);
    }

    public ConcurrentModificationException(String message, Throwable cause) {
        super(message, cause);
    }

    public ConcurrentModificationException(Throwable cause) {
        super(cause);
    }
}
