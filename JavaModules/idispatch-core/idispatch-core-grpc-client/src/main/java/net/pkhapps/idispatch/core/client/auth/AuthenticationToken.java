package net.pkhapps.idispatch.core.client.auth;

import org.jetbrains.annotations.NotNull;

import java.time.Instant;

/**
 * Interface declaring an authentication token that can be used to identify and authenticate an iDispatch Core user.
 *
 * @see AuthenticationProcess#authenticate()
 * @see AuthenticatedPrincipal
 */
public interface AuthenticationToken {

    /**
     * Returns the instant from which the token is valid.
     *
     * @return the instant from which the token is valid.
     */
    @NotNull Instant getTokenValidFrom();

    /**
     * Returns the instant from which the token is no longer valid.
     *
     * @return the instant from which the token is no longer valid.
     */
    @NotNull Instant getTokenValidTo();
}
