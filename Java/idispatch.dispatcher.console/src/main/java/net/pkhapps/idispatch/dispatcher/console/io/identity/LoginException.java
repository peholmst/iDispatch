package net.pkhapps.idispatch.dispatcher.console.io.identity;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;

import static java.util.Objects.requireNonNull;

/**
 * TODO Document me
 */
public class LoginException extends IOException {

    private final ErrorType errorType;

    public LoginException(@NotNull ErrorType errorType) {
        this.errorType = requireNonNull(errorType);
    }

    public @NotNull ErrorType getErrorType() {
        return errorType;
    }

    public enum ErrorType {
        /**
         * The login failed because the client credentials (client id and/or client secret) were invalid.
         */
        INVALID_CLIENT_CREDENTIALS,
        /**
         * The login failed because the user credentials (username and/or password) were invalid.
         */
        INVALID_USER_CREDENTALS,
        /**
         * The login failed because of a communications error.
         */
        COMMUNICATION_ERROR
    }

}
