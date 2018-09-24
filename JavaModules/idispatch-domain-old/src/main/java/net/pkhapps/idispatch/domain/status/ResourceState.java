package net.pkhapps.idispatch.domain.status;

/**
 * Enumeration of the states a resource can be in.
 */
public enum ResourceState {
    /**
     * The resource is reserved for an assignment, but has not yet been dispatched.
     */
    RESERVED(false),
    /**
     * The resource has been dispatched to an assignment.
     */
    DISPATCHED(false),
    /**
     * The resource is en route to the scene.
     */
    EN_ROUTE(false),
    /**
     * The resource is on scene.
     */
    ON_SCENE(false),
    /**
     * The resource is available for new assignments.
     */
    AVAILABLE(true),
    /**
     * The resource is at its station, available for new assignments.
     */
    AT_STATION(true),
    /**
     * The resource is out of service.
     */
    OUT_OF_SERVICE(false);

    private final boolean availableForNewAssignments;

    ResourceState(boolean availableForNewAssignments) {
        this.availableForNewAssignments = availableForNewAssignments;
    }

    /**
     * TODO Document me
     *
     * @return
     */
    public boolean isAvailableForNewAssignments() {
        return availableForNewAssignments;
    }
}
