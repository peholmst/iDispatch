package net.pkhapps.idispatch.dispatcher.console.io;

import org.jetbrains.annotations.NotNull;

/**
 * Base class for immutable implementations of {@link Server}.
 */
public abstract class AbstractServer implements Server {

    private final String name;
    private final String clientId;
    private final String clientSecret;

    protected AbstractServer(@NotNull String name, @NotNull String clientId, @NotNull String clientSecret) {
        this.name = name;
        this.clientId = clientId;
        this.clientSecret = clientSecret;
    }

    @Override
    public @NotNull String getName() {
        return name;
    }

    @Override
    public @NotNull String getClientId() {
        return clientId;
    }

    @Override
    public @NotNull String getClientSecret() {
        return clientSecret;
    }
}
