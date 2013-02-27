package net.pkhsolutions.idispatch.ejb.common;

import javax.ejb.ApplicationException;

@ApplicationException(rollback = true)
public class SaveFailedException extends Exception {

    public SaveFailedException() {
    }

    public SaveFailedException(String message) {
        super(message);
    }

    public SaveFailedException(String message, Throwable cause) {
        super(message, cause);
    }

    public SaveFailedException(Throwable cause) {
        super(cause);
    }
}
