package net.pkhsolutions.idispatch.ejb.common;

import javax.ejb.ApplicationException;

@ApplicationException(rollback = true)
public class DeletionFailedException extends Exception {

    public DeletionFailedException() {
    }

    public DeletionFailedException(String message) {
        super(message);
    }

    public DeletionFailedException(String message, Throwable cause) {
        super(message, cause);
    }

    public DeletionFailedException(Throwable cause) {
        super(cause);
    }
}
