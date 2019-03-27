package net.pkhapps.idispatch.dispatcher.console.io;

import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableObjectValue;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * TODO Document me!
 */
public class SystemStatus {

    private static final Logger LOGGER = LoggerFactory.getLogger(SystemStatus.class);
    private final String systemName;
    private final SimpleObjectProperty<State> stateProperty = new SimpleObjectProperty<>(State.UNKNOWN);

    public SystemStatus(String systemName) {
        this.systemName = systemName;
    }

    public @NotNull String getSystemName() {
        return systemName;
    }

    @NotNull
    public State getState() {
        return stateProperty.get();
    }

    void setState(State state) {
        LOGGER.debug("System [{}] state is: {}", systemName, state);
        stateProperty.set(state);
    }

    public @NotNull ObservableObjectValue<State> stateProperty() {
        return stateProperty;
    }

    public enum State {
        ONLINE, OFFLINE, UNKNOWN;

        public boolean isOnline() {
            return this == ONLINE;
        }

        public boolean isOffline() {
            return this == OFFLINE;
        }
    }
}
