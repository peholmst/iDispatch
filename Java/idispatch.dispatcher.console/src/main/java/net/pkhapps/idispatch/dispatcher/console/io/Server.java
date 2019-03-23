package net.pkhapps.idispatch.dispatcher.console.io;

import org.jetbrains.annotations.NotNull;

import java.net.URI;

/**
 * TODO Document me!
 */
public interface Server {

    @NotNull String getName();

    @NotNull String getClientId();

    @NotNull String getClientSecret();

    boolean isSecure();

    @NotNull URI getIdentityServerUri();

    @NotNull URI getCadServerUri();

    @NotNull URI getGisServerUri();
}
