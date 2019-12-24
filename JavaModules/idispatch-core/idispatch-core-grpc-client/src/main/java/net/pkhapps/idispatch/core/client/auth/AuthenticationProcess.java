package net.pkhapps.idispatch.core.client.auth;

import org.jetbrains.annotations.NotNull;

import javax.security.auth.Subject;

/**
 * Interface defining a process for authenticating a particular user. An interface is used for this purpose to allow for
 * multiple request-responses between the client and the server and make it easier to implement e.g. two-factor
 * authentication. Once all the necessary information has been collected, the caller should call {@link #authenticate()}
 * to perform the actual authentication.
 */
public interface AuthenticationProcess {

    // TODO Add 2f authentication

    /**
     * Sets the password to use for authentication.
     *
     * @param password the password.
     * @return this authentication process.
     */
    @NotNull AuthenticationProcess setPassword(@NotNull String password);

    /**
     * Attempts to authenticate the user.
     *
     * @return a {@link Subject} containing the user's {@link AuthenticatedPrincipal} and {@link AuthenticationToken}.
     * @throws AuthenticationException if authentication failed or timed out.
     */
    @NotNull Subject authenticate() throws AuthenticationException;
}
