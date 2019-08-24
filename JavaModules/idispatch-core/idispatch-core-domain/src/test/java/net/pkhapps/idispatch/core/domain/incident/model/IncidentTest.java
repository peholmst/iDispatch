package net.pkhapps.idispatch.core.domain.incident.model;

import net.pkhapps.idispatch.core.domain.common.AggregateRootTestBase;
import net.pkhapps.idispatch.core.domain.geo.Location;
import org.geotools.geometry.DirectPosition2D;
import org.jetbrains.annotations.NotNull;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.reset;

/**
 * Unit test for {@link Incident}.
 */
public class IncidentTest extends AggregateRootTestBase {

    private IncidentRepository repositoryMock;

    private static void assertNewState(Incident incident) {
        assertThat(incident.state()).isEqualTo(IncidentState.NEW);
    }

    private static void assertStateChange(Incident incident, IncidentState expected) {
        assertThat(incident.state()).isEqualTo(expected);
        assertPublishedDomainEvent(incident, IncidentStateChangedEvent.class,
                event -> expected.equals(event.state()));
    }

    @BeforeTest
    public void setUpTest() {
        repositoryMock = mock(IncidentRepository.class);
    }

    @BeforeMethod
    public void setUpTestMethod() {
        reset(repositoryMock);
    }

    @Test
    @Override
    public void initialState_newlyCreatedAggregateIsInValidState() {
        final var id = IncidentId.random();
        final var incident = new Incident(id);

        assertNewState(incident);
        assertThat(incident.id()).isEqualTo(id);
        assertThat(incident.parent()).isEmpty();
        assertThat(incident.location()).isEmpty();
        assertThat(incident.type()).isEmpty();
        assertThat(incident.priority()).isEqualTo(IncidentPriority.NO_PRIORITY);
        assertThat(incident.onHoldReason()).isEmpty();
        assertPublishedDomainEvent(incident, IncidentOpenedEvent.class, event -> id.equals(event.incident()));
    }

    @Test
    public void pinpoint_incidentIsNew_stateRemainsUnchanged() {
        final var incident = createIncident();
        final var location = createLocation();

        incident.pinpoint(location);

        assertNewState(incident);
        assertThat(incident.location()).contains(location);
        assertPublishedDomainEvent(incident, IncidentPinpointedEvent.class, event -> location.equals(event.location()));
    }

    @Test
    public void categorize_incidentIsNew_stateRemainsUnchanged() {
        final var incident = createIncident();
        final var type = createType();
        final var priority = IncidentPriority.A;

        incident.categorize(type, priority);

        assertNewState(incident);
        assertThat(incident.type()).contains(type);
        assertThat(incident.priority()).isEqualTo(priority);
        assertPublishedDomainEvent(incident, IncidentCategorizedEvent.class, event -> type.equals(event.type())
                && priority.equals(event.priority()));
    }

    @Test
    public void pinpoint_incidentIsCategorized_readyForDispatch() {
        final var incident = createIncident();
        incident.categorize(createType(), IncidentPriority.A);
        incident.pinpoint(createLocation());

        assertStateChange(incident, IncidentState.READY_FOR_DISPATCH);
    }

    @Test
    public void categorize_incidentIsPinpointed_readyForDispatch() {
        final var incident = createIncident();
        incident.pinpoint(createLocation());
        incident.categorize(createType(), IncidentPriority.A);

        assertStateChange(incident, IncidentState.READY_FOR_DISPATCH);
    }

    @Test
    public void resourcesHaveBeenDispatched_incidentIsNew_noChange() {
        final var incident = createIncident();
        incident.resourcesHaveBeenDispatched();

        assertNewState(incident);
    }

    @Test
    public void resourcesHaveBeenDispatched_incidentIsReadyForDispatch_stateChanges() {
        final var incident = createIncident();
        incident.pinpoint(createLocation());
        incident.categorize(createType(), IncidentPriority.B);
        incident.resourcesHaveBeenDispatched();

        assertStateChange(incident, IncidentState.DISPATCHED);
    }

    @Test
    public void resourcesHaveBeenDispatched_resourcesAreOnScene_noChange() {
        final var incident = createIncident();
        incident.pinpoint(createLocation());
        incident.categorize(createType(), IncidentPriority.B);
        incident.resourcesHaveBeenDispatched();
        incident.resourcesAreOnScene();
        incident.resourcesHaveBeenDispatched(); // Again

        assertStateChange(incident, IncidentState.RESOURCES_ON_SCENE);
    }

    @Test
    public void resourcesHaveBeenDispatched_resourcesHaveClearedTheScene_stateChanges() {
        final var incident = createIncident();
        incident.pinpoint(createLocation());
        incident.categorize(createType(), IncidentPriority.B);
        incident.resourcesHaveBeenDispatched();
        incident.resourcesAreOnScene();
        incident.resourcesHaveClearedTheScene();
        incident.resourcesHaveBeenDispatched(); // Again

        assertStateChange(incident, IncidentState.DISPATCHED);
    }

    @Test
    public void resourcesHaveBeenDispatched_incidentIsOnHold_stateChanges() {
        final var incident = createIncident();
        incident.pinpoint(createLocation());
        incident.categorize(createType(), IncidentPriority.B);
        incident.putOnHold("reason");
        incident.resourcesHaveBeenDispatched();

        assertStateChange(incident, IncidentState.DISPATCHED);
        assertThat(incident.onHoldReason()).isEmpty();
    }

    @Test
    public void resourcesHaveBeenDispatched_resourcesHaveAlreadyBeenDispatched_nothingHappens() {
        final var incident = createIncident();
        incident.pinpoint(createLocation());
        incident.categorize(createType(), IncidentPriority.B);
        incident.resourcesHaveBeenDispatched();
        clearDomainEvents(incident);

        incident.resourcesHaveBeenDispatched(); // again

        assertNoPublishedDomainEvents(incident);
    }

    @Test
    public void resourcesAreOnScene_incidentIsNew_noChange() {
        final var incident = createIncident();
        incident.resourcesAreOnScene();

        assertNewState(incident);
    }

    @Test
    public void resourcesAreOnScene_incidentIsReadyForDispatch_stateChanges() {
        final var incident = createIncident();
        incident.pinpoint(createLocation());
        incident.categorize(createType(), IncidentPriority.B);
        incident.resourcesAreOnScene();

        assertStateChange(incident, IncidentState.RESOURCES_ON_SCENE);
    }

    @Test
    public void resourcesAreOnScene_incidentIsDispatched_stateChanges() {
        final var incident = createIncident();
        incident.pinpoint(createLocation());
        incident.categorize(createType(), IncidentPriority.B);
        incident.resourcesHaveBeenDispatched();
        incident.resourcesAreOnScene();

        assertStateChange(incident, IncidentState.RESOURCES_ON_SCENE);
    }

    @Test
    public void resourcesAreOnScene_resourcesHaveClearedTheScene_stateChanges() {
        final var incident = createIncident();
        incident.pinpoint(createLocation());
        incident.categorize(createType(), IncidentPriority.B);
        incident.resourcesHaveBeenDispatched();
        incident.resourcesAreOnScene();
        incident.resourcesHaveClearedTheScene();
        incident.resourcesAreOnScene(); // again

        assertStateChange(incident, IncidentState.RESOURCES_ON_SCENE);
    }

    @Test
    public void resourcesAreOnScene_incidentIsOnHold_stateChanges() {
        final var incident = createIncident();
        incident.pinpoint(createLocation());
        incident.categorize(createType(), IncidentPriority.B);
        incident.putOnHold("reason");
        incident.resourcesAreOnScene();

        assertStateChange(incident, IncidentState.RESOURCES_ON_SCENE);
        assertThat(incident.onHoldReason()).isEmpty();
    }

    @Test
    public void resourcesAreOnScene_resourcesAreAlreadyOnScene_nothingHappens() {
        final var incident = createIncident();
        incident.pinpoint(createLocation());
        incident.categorize(createType(), IncidentPriority.B);
        incident.resourcesAreOnScene();
        clearDomainEvents(incident);

        incident.resourcesAreOnScene(); // again

        assertNoPublishedDomainEvents(incident);
    }

    @Test
    public void resourcesHaveClearedTheScene_incidentIsNew_nothingChanges() {
        final var incident = createIncident();
        incident.resourcesHaveClearedTheScene();

        assertNewState(incident);
    }

    @Test
    public void resourcesHaveClearedTheScene_incidentIsReadyForDispatch_nothingChanges() {
        final var incident = createIncident();
        incident.pinpoint(createLocation());
        incident.categorize(createType(), IncidentPriority.B);
        incident.resourcesHaveClearedTheScene();

        assertStateChange(incident, IncidentState.READY_FOR_DISPATCH);
    }

    @Test
    public void resourcesHaveClearedTheScene_incidentIsDispatched_stateChangesBackToReadyForDispatch() {
        final var incident = createIncident();
        incident.pinpoint(createLocation());
        incident.categorize(createType(), IncidentPriority.B);
        incident.resourcesHaveBeenDispatched();
        incident.resourcesHaveClearedTheScene();

        assertStateChange(incident, IncidentState.READY_FOR_DISPATCH);
    }

    @Test
    public void resourcesHaveClearedTheScene_resourcesAreOnScene_stateChanges() {
        final var incident = createIncident();
        incident.pinpoint(createLocation());
        incident.categorize(createType(), IncidentPriority.B);
        incident.resourcesHaveBeenDispatched();
        incident.resourcesAreOnScene();
        incident.resourcesHaveClearedTheScene();

        assertStateChange(incident, IncidentState.CLEARED);
    }

    @Test
    public void resourcesHaveClearedTheScene_resourcesHaveAlreadyClearedTheScene_nothingHappens() {
        final var incident = createIncident();
        incident.pinpoint(createLocation());
        incident.categorize(createType(), IncidentPriority.B);
        incident.resourcesHaveBeenDispatched();
        incident.resourcesAreOnScene();
        incident.resourcesHaveClearedTheScene();
        clearDomainEvents(incident);

        incident.resourcesHaveClearedTheScene(); // again

        assertNoPublishedDomainEvents(incident);
    }


    @Test(expectedExceptions = IllegalIncidentStateTransitionException.class)
    public void putOnHold_incidentIsNew_exceptionThrown() {
        final var incident = createIncident();
        incident.putOnHold("reason");
    }

    @Test
    public void putOnHold_incidentIsReadyForDispatch_stateChanges() {
        final var incident = createIncident();
        incident.pinpoint(createLocation());
        incident.categorize(createType(), IncidentPriority.B);
        incident.putOnHold("reason");

        assertStateChange(incident, IncidentState.ON_HOLD);
        assertThat(incident.onHoldReason()).contains("reason");
    }

    @Test(expectedExceptions = IllegalIncidentStateTransitionException.class)
    public void putOnHold_incidentIsDispatched_exceptionThrown() {
        final var incident = createIncident();
        incident.pinpoint(createLocation());
        incident.categorize(createType(), IncidentPriority.B);
        incident.resourcesHaveBeenDispatched();
        incident.putOnHold("reason");
    }

    @Test(expectedExceptions = IllegalIncidentStateTransitionException.class)
    public void putOnHold_resourcesAreOnScene_exceptionThrown() {
        final var incident = createIncident();
        incident.pinpoint(createLocation());
        incident.categorize(createType(), IncidentPriority.B);
        incident.resourcesHaveBeenDispatched();
        incident.resourcesAreOnScene();
        incident.putOnHold("reason");
    }

    @Test
    public void putOnHold_resourcesHaveClearedTheScene_stateChanges() {
        final var incident = createIncident();
        incident.pinpoint(createLocation());
        incident.categorize(createType(), IncidentPriority.B);
        incident.resourcesHaveBeenDispatched();
        incident.resourcesAreOnScene();
        incident.resourcesHaveClearedTheScene();
        incident.putOnHold("reason");

        assertStateChange(incident, IncidentState.ON_HOLD);
        assertThat(incident.onHoldReason()).contains("reason");
    }

    @Test
    public void putOnHold_alreadyOnHold_nothingHappens() {
        final var incident = createIncident();
        incident.pinpoint(createLocation());
        incident.categorize(createType(), IncidentPriority.B);
        incident.putOnHold("reason");
        clearDomainEvents(incident);

        incident.putOnHold("reason 2");

        assertNoPublishedDomainEvents(incident);
        assertThat(incident.onHoldReason()).contains("reason");
    }

    private @NotNull Incident createIncident() {
        return new Incident(IncidentId.random());
    }

    private @NotNull Location createLocation() {
        return new Location(new DirectPosition2D(60.286027, 60.286027), null, null) {
        };
    }

    private @NotNull IncidentTypeId createType() {
        return new IncidentTypeId();
    }
}
