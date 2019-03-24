package net.pkhapps.idispatch.dispatcher.console.main;

import javafx.scene.Group;
import javafx.scene.Scene;
import net.pkhapps.idispatch.dispatcher.console.io.Server;
import net.pkhapps.idispatch.dispatcher.console.io.identity.User;
import org.jetbrains.annotations.NotNull;

/**
 * Main application window.
 */
public class MainWindowScene extends Scene {

    public MainWindowScene(@NotNull User user, @NotNull Server server) {
        super(new Group()); // TODO Implement me
    }
}
