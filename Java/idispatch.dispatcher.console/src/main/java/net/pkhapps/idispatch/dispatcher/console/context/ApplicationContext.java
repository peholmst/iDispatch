package net.pkhapps.idispatch.dispatcher.console.context;

import net.pkhapps.idispatch.dispatcher.console.io.Server;
import net.pkhapps.idispatch.dispatcher.console.io.SystemStatusSummary;
import net.pkhapps.idispatch.dispatcher.console.io.identity.User;
import org.jetbrains.annotations.NotNull;

import static java.util.Objects.requireNonNull;

/**
 * TODO Document me!
 */
public class ApplicationContext {

    private final Server server;
    private final User user;
    private final SystemStatusSummary systemStatusSummary;

    public ApplicationContext(@NotNull Server server, @NotNull User user, @NotNull SystemStatusSummary systemStatusSummary) {
        this.server = requireNonNull(server);
        this.user = requireNonNull(user);
        this.systemStatusSummary = requireNonNull(systemStatusSummary);
    }

    @NotNull
    public Server getServer() {
        return server;
    }

    @NotNull
    public User getUser() {
        return user;
    }

    @NotNull
    public SystemStatusSummary getSystemStatusSummary() {
        return systemStatusSummary;
    }
}
