package net.pkhsolutions.idispatch.entity;

/**
 * Enumeration of the states a resource can be in.
 */
public enum ResourceState {
    ASSIGNED(false), DISPATCHED(false), EN_ROUTE(false), ON_SCENE(false), AVAILABLE(true), AT_STATION(true), UNAVAILABLE(false);

    private final boolean availableForNewTickets;

    ResourceState(boolean availableForNewTickets) {
        this.availableForNewTickets = availableForNewTickets;
    }

    public boolean isAvailableForNewTickets() {
        return availableForNewTickets;
    }
}
