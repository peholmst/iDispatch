package net.pkhapps.idispatch.core.domain.incident.model;

import net.pkhapps.idispatch.core.domain.common.AggregateRoot;
import net.pkhapps.idispatch.core.domain.geo.Location;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

import static java.util.Objects.requireNonNull;

/**
 * TODO Document me!
 */
public class Incident extends AggregateRoot<IncidentId> {

    private IncidentId parent;
    private IncidentState state = IncidentState.NEW;
    private Location location;
    private IncidentTypeId type;
    private IncidentPriority priority = IncidentPriority.NO_PRIORITY;
    private String onHoldReason;

    Incident(@NotNull IncidentId incidentId) {
        super(incidentId);
        publishEvent(new IncidentOpenedEvent(this));
    }

    /**
     * The parent of the incident if it has one.
     */
    public @NotNull Optional<IncidentId> parent() {
        return Optional.ofNullable(parent);
    }

    /**
     * The state of the incident.
     */
    public @NotNull IncidentState state() {
        return state;
    }

    private void setState(@NotNull IncidentState state) {
        // This method does not care whether the state transition is legal or not. That has to be handled
        // by the calling method.
        if (!state.equals(this.state)) {
            this.state = state;
            publishEvent(new IncidentStateChangedEvent(this));
            if (state != IncidentState.ON_HOLD) {
                this.onHoldReason = null;
            }
        }
    }

    /**
     * The location of the incident if pinpointed.
     */
    public @NotNull Optional<Location> location() {
        return Optional.ofNullable(location);
    }

    /**
     * The type of the incident if categorized.
     */
    public @NotNull Optional<IncidentTypeId> type() {
        return Optional.ofNullable(type);
    }

    /**
     * The priority of the incident.
     */
    public @NotNull IncidentPriority priority() {
        return priority;
    }

    /**
     * The reason for why the incident is on hold.
     */
    public @NotNull Optional<String> onHoldReason() {
        return Optional.ofNullable(onHoldReason);
    }

    /**
     * Pinpoints the location of the incident.
     *
     * @param location the new location of the incident.
     */
    public void pinpoint(@NotNull Location location) {
        this.location = requireNonNull(location);
        publishEvent(new IncidentPinpointedEvent(this));
        checkIfReadyForDispatch();
    }

    /**
     * Categorizes the incident.
     *
     * @param type     the type of the incident.
     * @param priority the priority of the incident.
     */
    public void categorize(@NotNull IncidentTypeId type, @NotNull IncidentPriority priority) {
        this.type = requireNonNull(type);
        this.priority = requireNonNull(priority);
        publishEvent(new IncidentCategorizedEvent(this));
        checkIfReadyForDispatch();
    }

    /**
     * Informs the aggregate that resources have been dispatched to the location of the incident. This may or may
     * not change the state of the aggregate.
     */
    public void resourcesHaveBeenDispatched() {
        if (state == IncidentState.READY_FOR_DISPATCH || state == IncidentState.CLEARED
                || state == IncidentState.ON_HOLD) {
            setState(IncidentState.DISPATCHED);
        }
    }

    /**
     * Informs the aggregate that resources are on the scene of the incident. This may or may not change the state of
     * the aggregate.
     */
    public void resourcesAreOnScene() {
        if (state == IncidentState.READY_FOR_DISPATCH || state == IncidentState.DISPATCHED
                || state == IncidentState.CLEARED || state == IncidentState.ON_HOLD) {
            setState(IncidentState.RESOURCES_ON_SCENE);
        }
    }

    /**
     * Informs the aggregate that no resources are on the scene of the incident any longer, nor are there any resources
     * dispatched to the scene. This may or may not change the state of the aggregate.
     */
    public void resourcesHaveClearedTheScene() {
        if (state == IncidentState.DISPATCHED) {
            // This means the incidents were dispatched but for some reason never reached the scene of the incident.
            // Maybe they were cancelled, maybe they were re-routed to higher priority incident.
            setState(IncidentState.READY_FOR_DISPATCH);
        } else if (state == IncidentState.RESOURCES_ON_SCENE) {
            // Resources have been on the scene and left.
            setState(IncidentState.CLEARED);
        }
    }

    /**
     * Puts the incident on hold.
     *
     * @param reason the reason why the incident is on hold.
     * @throws IllegalIncidentStateTransitionException if the incident cannot be put on hold right now.
     */
    public void putOnHold(@NotNull String reason) {
        if (state == IncidentState.ON_HOLD) {
            return;
        }

        if (state == IncidentState.READY_FOR_DISPATCH || state == IncidentState.CLEARED) {
            this.onHoldReason = requireNonNull(reason);
            setState(IncidentState.ON_HOLD);
        } else {
            throw new IllegalIncidentStateTransitionException();
        }
    }

    /**
     * Closes the incident.
     *
     * @param incidentRepository the repository to use to check for sub-incidents.
     * @throws IncidentNotClearedException if the incident is not cleared or if there are sub-incidents that are still
     *                                     open.
     */
    public void close(@NotNull IncidentRepository incidentRepository) {
        state = IncidentState.CLOSED;
    }

    private void checkIfReadyForDispatch() {
        if (state == IncidentState.NEW && location != null && type != null) {
            setState(IncidentState.READY_FOR_DISPATCH);
        }
    }

    public @NotNull Incident createSubIncident(@NotNull IncidentFactory incidentFactory) {
        if (state().isClosed()) {
            throw new IncidentNotOpenException();
        }
        final var subIncident = incidentFactory.createIncident();
        subIncident.parent = id();
        return subIncident;
    }
}
