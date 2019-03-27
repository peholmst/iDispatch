package net.pkhapps.idispatch.dispatcher.console.io;

import javafx.beans.binding.Bindings;
import javafx.beans.value.ObservableObjectValue;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.stream.Stream;

/**
 * TODO Document me!
 */
public class SystemStatusSummary {

    private static final Logger LOGGER = LoggerFactory.getLogger(SystemStatusSummary.class);

    private final SystemStatus cadServer = new SystemStatus("CAD Server");
    private final SystemStatus gisServer = new SystemStatus("GIS Server");
    private final SystemStatus identityServer = new SystemStatus("Identity Server");
    private final ObservableObjectValue<State> state;

    public SystemStatusSummary() {
        state = Bindings.createObjectBinding(this::computeState,
                cadServer.stateProperty(),
                gisServer.stateProperty(),
                identityServer.stateProperty());
    }

    private @NotNull State computeState() {
        var cadState = cadServer.getState();
        var gisState = gisServer.getState();
        var identityState = identityServer.getState();

        State result;
        if (cadState.isOnline() && gisState.isOnline() && identityState.isOnline()) {
            result = State.ALL_SYSTEMS_OPERATIONAL;
        } else if (cadState.isOnline() || gisState.isOnline() || identityState.isOnline()) {
            result = State.SOME_SYSTEMS_OPERATIONAL;
        } else if (cadState.isOffline() && gisState.isOffline() && identityState.isOffline()) {
            result = State.NO_SYSTEMS_OPERATIONAL;
        } else {
            result = State.UNKNOWN;
        }
        LOGGER.debug("Recomputed system state: {}", result);
        return result;
    }

    public @NotNull ObservableObjectValue<State> state() {
        return state;
    }

    @NotNull SystemStatus getCadServer() {
        return cadServer;
    }

    @NotNull SystemStatus getGisServer() {
        return gisServer;
    }

    @NotNull SystemStatus getIdentityServer() {
        return identityServer;
    }

    public @NotNull Stream<SystemStatus> getSystemStatus() {
        return Stream.of(gisServer, cadServer, identityServer);
    }

    public enum State {
        ALL_SYSTEMS_OPERATIONAL,
        SOME_SYSTEMS_OPERATIONAL,
        NO_SYSTEMS_OPERATIONAL,
        UNKNOWN
    }
}
