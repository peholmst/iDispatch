package net.pkhapps.idispatch.core.client.auth;

/**
 * Exception thrown by {@link AuthenticationProcess} if authentication fails.
 */
public class AuthenticationException extends RuntimeException {

    public AuthenticationException(String message) {
        super(message);
    }

    public AuthenticationException(String message, Throwable cause) {
        super(message, cause);
    }
}
