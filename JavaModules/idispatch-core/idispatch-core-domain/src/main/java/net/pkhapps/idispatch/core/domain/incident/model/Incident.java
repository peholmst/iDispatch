package net.pkhapps.idispatch.core.domain.incident.model;

import net.pkhapps.idispatch.core.domain.common.AggregateRoot;
import net.pkhapps.idispatch.core.domain.common.PhoneNumber;
import net.pkhapps.idispatch.core.domain.geo.Location;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.time.Instant;
import java.util.Objects;
import java.util.Optional;

import static java.util.Objects.requireNonNull;

/**
 * Aggregate root representing an incident.
 * <p>
 * An incident is opened when a dispatcher receives a report from an informer
 * that something has happened. Once the incident has been {@link #pinpoint(Location) pinpointed} and
 * {@link #categorize(IncidentTypeId, IncidentPriority) categorized}, resources can be
 * {@link #resourcesHaveBeenDispatched() dispatched} to the scene of the incident. Resources will (hopefully)
 * {@link #resourcesAreOnScene() reach} the scene of the incident and eventually
 * {@link #resourcesHaveClearedTheScene() clear} it. If needed, the dispatcher can also put the incident
 * {@link #putOnHold(String) on hold}.
 * <p>
 * This aggregate acts as a state machine and keeps track of the different states an incident transitions to.
 * Domain events are published for the most important changes. The aggregate does not keep track of which
 * resources are actually dispatched to the incident.
 * <p>
 * Once an incident has been cleared, it can be {@link #close(IncidentRepository) closed}.
 */
public class Incident extends AggregateRoot<IncidentId> {

    private final Instant openedOn;
    private IncidentState state = IncidentState.NEW;
    private Location location;
    private IncidentTypeId type;
    private IncidentPriority priority = IncidentPriority.NO_PRIORITY;
    private String onHoldReason;
    private Instant closedOn;
    private String details;
    private String informerName;
    private PhoneNumber informerPhoneNumber;

    Incident(@NotNull IncidentId incidentId) {
        super(incidentId);
        final var openedEvent = new IncidentOpenedEvent(this);
        openedOn = openedEvent.occurredOn();
        publishEvent(openedEvent);
    }

    Incident(@NotNull Essence essence) {
        super(essence);
        openedOn = essence.openedOn;
        state = essence.state;
        location = essence.location;
        type = essence.type;
        priority = essence.priority;
        onHoldReason = essence.onHoldReason;
        closedOn = essence.closedOn;
        details = essence.details;
        informerName = essence.informerName;
        informerPhoneNumber = essence.informerPhoneNumber;
    }

    /**
     * The date and time on which the incident was opened.
     */
    public @NotNull Instant openedOn() {
        return openedOn;
    }

    /**
     * The date and time on which the incident was closed.
     */
    public @NotNull Optional<Instant> closedOn() {
        return Optional.ofNullable(closedOn);
    }

    /**
     * The parent of the incident if it has one.
     */
    public @NotNull Optional<IncidentId> parent() {
        return Optional.empty();
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
     * Any additional details about the incident.
     */
    public @NotNull Optional<String> details() {
        return Optional.ofNullable(details);
    }

    /**
     * The name of the informer that called in the incident.
     */
    public @NotNull Optional<String> informerName() {
        return Optional.ofNullable(informerName);
    }

    /**
     * The phone number of the informer that called in the incident.
     */
    public @NotNull Optional<PhoneNumber> informerPhoneNumber() {
        return Optional.ofNullable(informerPhoneNumber);
    }

    /**
     * Pinpoints the location of the incident.
     *
     * @param location the new location of the incident.
     * @throws IncidentNotOpenException if the incident is not open.
     */
    public void pinpoint(@NotNull Location location) {
        requireOpen();
        this.location = requireNonNull(location);
        publishEvent(new IncidentPinpointedEvent(this));
        checkIfReadyForDispatch();
    }

    /**
     * Categorizes the incident.
     *
     * @param type     the type of the incident.
     * @param priority the priority of the incident.
     * @throws IncidentNotOpenException if the incident is not open.
     */
    public void categorize(@NotNull IncidentTypeId type, @NotNull IncidentPriority priority) {
        requireOpen();
        this.type = requireNonNull(type);
        this.priority = requireNonNull(priority);
        publishEvent(new IncidentCategorizedEvent(this));
        checkIfReadyForDispatch();
    }

    /**
     * Informs the aggregate that resources have been dispatched to the location of the incident. This may or may
     * not change the state of the aggregate.
     *
     * @throws IncidentNotOpenException if the incident is not open.
     */
    public void resourcesHaveBeenDispatched() {
        requireOpen();
        if (state == IncidentState.READY_FOR_DISPATCH || state == IncidentState.CLEARED
                || state == IncidentState.ON_HOLD) {
            setState(IncidentState.DISPATCHED);
        }
    }

    /**
     * Informs the aggregate that resources are on the scene of the incident. This may or may not change the state of
     * the aggregate.
     *
     * @throws IncidentNotOpenException if the incident is not open.
     */
    public void resourcesAreOnScene() {
        requireOpen();
        if (state == IncidentState.READY_FOR_DISPATCH || state == IncidentState.DISPATCHED
                || state == IncidentState.CLEARED || state == IncidentState.ON_HOLD) {
            setState(IncidentState.RESOURCES_ON_SCENE);
        }
    }

    /**
     * Informs the aggregate that no resources are on the scene of the incident any longer, nor are there any resources
     * dispatched to the scene. This may or may not change the state of the aggregate.
     *
     * @throws IncidentNotOpenException if the incident is not open.
     */
    public void resourcesHaveClearedTheScene() {
        requireOpen();
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
     * @throws IncidentNotOpenException                if the incident is not open.
     */
    public void putOnHold(@NotNull String reason) {
        requireOpen();

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
        if (state.isClosed()) {
            return;
        }
        if (!state.canClose()) {
            throw new IncidentNotClearedException();
        }
        state = IncidentState.CLOSED;
        final var closedEvent = new IncidentClosedEvent(this);
        closedOn = closedEvent.occurredOn();
        publishEvent(closedEvent);
    }

    /**
     * Updates the details of this incident.
     *
     * @param details additional details about the incident.
     * @throws IncidentNotOpenException if the incident is not open.
     */
    public void updateDetails(@NotNull String details) {
        requireOpen();
        if (!Objects.equals(details, this.details)) {
            this.details = requireNonNull(details);
            publishEvent(new IncidentDetailsUpdatedEvent(this));
        }
    }

    /**
     * Updates the details about the informer that called in this incident.
     *
     * @param name        the name of the informer, if known or applicable.
     * @param phoneNumber the phone number of the informer, if known or applicable.
     * @throws IncidentNotOpenException if the incident is not open.
     */
    public void updateInformerDetails(@Nullable String name, @Nullable PhoneNumber phoneNumber) {
        requireOpen();
        if (!Objects.equals(name, informerName) || !Objects.equals(phoneNumber, informerPhoneNumber)) {
            this.informerName = name;
            this.informerPhoneNumber = phoneNumber;
            publishEvent(new IncidentInformerDetailsUpdatedEvent(this));
        }
    }

    private void checkIfReadyForDispatch() {
        if (state == IncidentState.NEW && location != null && type != null) {
            setState(IncidentState.READY_FOR_DISPATCH);
        }
    }

    private void requireOpen() {
        if (state().isClosed()) {
            throw new IncidentNotOpenException();
        }
    }

    /**
     * Creates a new sub-incident of this incident. The sub-incident will initially contain the same information as
     * this incident (but with different IDs and timestamps of course).
     *
     * @param incidentFactory the factory to use when creating the new {@link Incident} instance.
     * @return the sub-incident.
     * @throws IncidentNotOpenException if the incident is not open.
     */
    public @NotNull Incident createSubIncident(@NotNull IncidentFactory incidentFactory) {
        // TODO Implement createSubIncident
        throw new UnsupportedOperationException("Not implemented yet");
    }

    /**
     * Essence class to be used by repositories when creating populating instances of {@link Incident}.
     */
    public static class Essence extends AggregateRoot.Essence<IncidentId> {

        private Instant openedOn;
        private IncidentState state;
        private Location location;
        private IncidentTypeId type;
        private IncidentPriority priority;
        private String onHoldReason;
        private Instant closedOn;
        private String details;
        private String informerName;
        private PhoneNumber informerPhoneNumber;

        Essence() {
        }

        Essence(@NotNull Incident source) {
            super(source);
            openedOn = source.openedOn;
            state = source.state;
            location = source.location;
            type = source.type;
            priority = source.priority;
            onHoldReason = source.onHoldReason;
            closedOn = source.closedOn;
            details = source.details;
            informerName = source.informerName;
            informerPhoneNumber = source.informerPhoneNumber;
        }

        public Instant getOpenedOn() {
            return openedOn;
        }

        public void setOpenedOn(Instant openedOn) {
            this.openedOn = openedOn;
        }

        public IncidentState getState() {
            return state;
        }

        public void setState(IncidentState state) {
            this.state = state;
        }

        public Location getLocation() {
            return location;
        }

        public void setLocation(Location location) {
            this.location = location;
        }

        public IncidentTypeId getType() {
            return type;
        }

        public void setType(IncidentTypeId type) {
            this.type = type;
        }

        public IncidentPriority getPriority() {
            return priority;
        }

        public void setPriority(IncidentPriority priority) {
            this.priority = priority;
        }

        public String getOnHoldReason() {
            return onHoldReason;
        }

        public void setOnHoldReason(String onHoldReason) {
            this.onHoldReason = onHoldReason;
        }

        public Instant getClosedOn() {
            return closedOn;
        }

        public void setClosedOn(Instant closedOn) {
            this.closedOn = closedOn;
        }

        public String getDetails() {
            return details;
        }

        public void setDetails(String details) {
            this.details = details;
        }

        public String getInformerName() {
            return informerName;
        }

        public void setInformerName(String informerName) {
            this.informerName = informerName;
        }

        public PhoneNumber getInformerPhoneNumber() {
            return informerPhoneNumber;
        }

        public void setInformerPhoneNumber(PhoneNumber informerPhoneNumber) {
            this.informerPhoneNumber = informerPhoneNumber;
        }

        @Override
        protected boolean isValid() {
            return super.isValid() && state != null && openedOn != null && priority != null;
        }
    }
}
