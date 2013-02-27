package net.pkhsolutions.idispatch.ejb.common;

import javax.ejb.ApplicationException;

@ApplicationException(rollback = true)
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
