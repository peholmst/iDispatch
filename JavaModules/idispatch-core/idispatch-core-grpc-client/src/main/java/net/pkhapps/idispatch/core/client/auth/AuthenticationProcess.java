package net.pkhapps.idispatch.core.client.auth;

import org.jetbrains.annotations.NotNull;

/**
 * TODO Document me
 */
public interface AuthenticationProcess {

    // TODO Add 2f authentication

    @NotNull AuthenticationProcess setPassword(@NotNull String password);

    @NotNull AuthenticatedPrincipal authenticate() throws AuthenticationException;
}
