package net.pkhapps.idispatch.dispatcher.console.io;

import javafx.collections.ObservableList;
import org.jetbrains.annotations.NotNull;

/**
 * TODO Document me!
 */
public interface ServerSettings {

    @NotNull ObservableList<Server> getServers();
}
