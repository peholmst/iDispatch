package net.pkhsolutions.idispatch.ejb.tickets;

import javax.ejb.ApplicationException;

@ApplicationException(rollback = true)
public class ResourceNotAvailableException extends Exception {

    public ResourceNotAvailableException() {
    }

    public ResourceNotAvailableException(String message) {
        super(message);
    }

    public ResourceNotAvailableException(String message, Throwable cause) {
        super(message, cause);
    }

    public ResourceNotAvailableException(Throwable cause) {
        super(cause);
    }
}
