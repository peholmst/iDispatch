package net.pkhapps.idispatch.dispatcher.console.io;

import org.jetbrains.annotations.NotNull;

import java.net.URI;

/**
 * Implementation of {@link Server} that points to the server applications running on the local machine. Intended
 * for development use only.
 */
public class LocalDevelopmentServer extends AbstractServer {

    private static final URI IDENTITY_SERVER_URI = URI.create("http://localhost:8081");
    private static final URI CAD_SERVER_URI = URI.create("http://localhost:8082");
    private static final URI GIS_SERVER_URI = URI.create("http://localhost:8083");

    public LocalDevelopmentServer() {
        super("Local Development Server", "local-dev-dispatcher-console", "notasecret");
    }

    @Override
    public boolean isSecure() {
        return false;
    }

    @Override
    public @NotNull URI getIdentityServerUri() {
        return IDENTITY_SERVER_URI;
    }

    @Override
    public @NotNull URI getCadServerUri() {
        return CAD_SERVER_URI;
    }

    @Override
    public @NotNull URI getGisServerUri() {
        return GIS_SERVER_URI;
    }
}
