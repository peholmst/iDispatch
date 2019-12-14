package net.pkhapps.idispatch.core.client.auth;

import org.jetbrains.annotations.NotNull;

import java.security.Principal;

/**
 * TODO Document me
 */
public interface AuthenticatedPrincipal extends Principal {

    @NotNull String getFullName();

    @NotNull String getDispatchCenterName();
}
