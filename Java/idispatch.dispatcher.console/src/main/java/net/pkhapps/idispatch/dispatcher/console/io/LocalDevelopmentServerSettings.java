package net.pkhapps.idispatch.dispatcher.console.io;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.jetbrains.annotations.NotNull;

/**
 * Implementation of {@link ServerSettings} that returns a single {@link LocalDevelopmentServer} instance. Intended
 * for development use only.
 */
public class LocalDevelopmentServerSettings implements ServerSettings {

    private final ObservableList<Server> servers = FXCollections.singletonObservableList(new LocalDevelopmentServer());

    @Override
    public @NotNull ObservableList<Server> getServers() {
        return servers;
    }
}
