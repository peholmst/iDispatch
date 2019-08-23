package net.pkhapps.idispatch.core.domain.incident.model;

/**
 * Enumeration of the states that an incident can be in.
 */
public enum IncidentState {
    /**
     * The incident is new and lacks the information needed to dispatch resources to the scene.
     */
    NEW,
    /**
     * The incident has enough information to dispatch resources to the scene but none have been dispatched yet.
     */
    READY_FOR_DISPATCH,
    /**
     * Resources have been dispatched to the scene but none have arrived yet.
     */
    DISPATCHED,
    /**
     * Resources have arrived at the scene of the incident.
     */
    RESOURCES_ON_SCENE,
    /**
     * All resources have cleared the incident.
     */
    CLEARED,
    /**
     * The incident has been manually put on hold by the dispatcher.
     */
    ON_HOLD,
    /**
     * The incident has been closed.
     */
    CLOSED;

    /**
     * Returns whether this state implies that the incident is open.
     */
    public boolean isOpen() {
        return this != CLOSED;
    }
}
