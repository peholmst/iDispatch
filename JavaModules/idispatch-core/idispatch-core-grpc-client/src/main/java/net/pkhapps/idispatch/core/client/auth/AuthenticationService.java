package net.pkhapps.idispatch.core.client.auth;

import org.jetbrains.annotations.NotNull;

/**
 * TODO Document me
 */
public interface AuthenticationService {

    @NotNull AuthenticationProcess startAuthentication(@NotNull String username);
}
