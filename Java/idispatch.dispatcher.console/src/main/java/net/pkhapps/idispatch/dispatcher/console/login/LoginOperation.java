package net.pkhapps.idispatch.dispatcher.console.login;

import net.pkhapps.idispatch.dispatcher.console.io.Server;
import net.pkhapps.idispatch.dispatcher.console.io.identity.LoginException;
import org.jetbrains.annotations.NotNull;

/**
 * TODO Document me!
 */
@FunctionalInterface
public interface LoginOperation {

    void login(@NotNull String username, @NotNull String password, @NotNull Server server) throws LoginException;
}
