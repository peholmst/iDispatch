package net.pkhapps.idispatch.core.domain.incident.model;

import net.pkhapps.idispatch.core.domain.common.AggregateRootTestBase;
import net.pkhapps.idispatch.core.domain.common.DomainContext;
import net.pkhapps.idispatch.core.domain.common.DomainContextHolder;
import net.pkhapps.idispatch.core.domain.common.PhoneNumber;
import net.pkhapps.idispatch.core.domain.geo.Location;
import org.jetbrains.annotations.NotNull;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.time.Clock;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

/**
 * Unit test for {@link Incident}.
 */
public class IncidentTest extends AggregateRootTestBase {

    private IncidentRepository repositoryMock;
    private IncidentFactory factoryMock;
    private DomainContext domainContextMock;
    private Clock clock;

    private static void assertNew(Incident incident) {
        assertThat(incident.state()).isEqualTo(IncidentState.NEW);
        assertThat(incident.closedOn()).isEmpty();
        assertPublishedDomainEvent(incident, IncidentOpenedEvent.class,
                event -> incident.id().equals(event.incident())
                        && incident.openedOn().equals(event.occurredOn()));
    }

    @SuppressWarnings("OptionalGetWithoutIsPresent")
    private static void assertClosed(Incident incident) {
        assertThat(incident.state()).isEqualTo(IncidentState.CLOSED);
        assertThat(incident.closedOn()).isPresent();
        assertPublishedDomainEvent(incident, IncidentClosedEvent.class,
                event -> incident.id().equals(event.incident())
                        && incident.closedOn().get().equals(event.occurredOn()));
    }

    private static void assertStateChange(Incident incident, IncidentState expected) {
        assertThat(incident.state()).isEqualTo(expected);
        assertPublishedDomainEvent(incident, IncidentStateChangedEvent.class,
                event -> expected.equals(event.state()));
    }

    @BeforeTest
    public void setUpTest() {
        repositoryMock = mock(IncidentRepository.class);
        factoryMock = mock(IncidentFactory.class);
        domainContextMock = mock(DomainContext.class);
        DomainContextHolder.setProvider(() -> domainContextMock);
    }

    @BeforeMethod
    public void setUpTestMethod() {
        clock = Clock.systemUTC();
        reset(repositoryMock, factoryMock, domainContextMock);
        when(domainContextMock.clock()).thenAnswer(invocationOnMock -> clock);
    }

    @Test
    @Override
    public void initialState_newlyCreatedAggregateIsInValidState() {
        final var id = new IncidentId(UUID.randomUUID());
        final var incident = new Incident(id);

        assertNew(incident);
        assertThat(incident.id()).isEqualTo(id);
        assertThat(incident.parent()).isEmpty();
        assertThat(incident.location()).isEmpty();
        assertThat(incident.type()).isEmpty();
        assertThat(incident.priority()).isEqualTo(IncidentPriority.NO_PRIORITY);
        assertThat(incident.onHoldReason()).isEmpty();
        assertThat(incident.details()).isEmpty();
        assertThat(incident.informerName()).isEmpty();
        assertThat(incident.informerPhoneNumber()).isEmpty();
    }

    @Test
    public void pinpoint_incidentIsNew_stateRemainsUnchanged() {
        final var incident = createIncident();
        final var location = createLocation();

        incident.pinpoint(location);

        assertNew(incident);
        assertThat(incident.location()).contains(location);
        assertPublishedDomainEvent(incident, IncidentPinpointedEvent.class, event -> location.equals(event.location()));
    }

    @Test
    public void pinpoint_incidentIsCategorized_readyForDispatch() {
        final var incident = createIncident();
        incident.categorize(createType(), IncidentPriority.A);
        incident.pinpoint(createLocation());

        assertStateChange(incident, IncidentState.READY_FOR_DISPATCH);
    }

    @Test(expectedExceptions = IncidentNotOpenException.class)
    public void pinpoint_incidentIsClosed_exceptionThrown() {
        final var incident = createIncident();
        incident.close(repositoryMock);
        incident.pinpoint(createLocation());
    }

    @Test
    public void categorize_incidentIsNew_stateRemainsUnchanged() {
        final var incident = createIncident();
        final var type = createType();
        final var priority = IncidentPriority.A;

        incident.categorize(type, priority);

        assertNew(incident);
        assertThat(incident.type()).contains(type);
        assertThat(incident.priority()).isEqualTo(priority);
        assertPublishedDomainEvent(incident, IncidentCategorizedEvent.class, event -> type.equals(event.type())
                && priority.equals(event.priority()));
    }

    @Test
    public void categorize_incidentIsPinpointed_readyForDispatch() {
        final var incident = createIncident();
        incident.pinpoint(createLocation());
        incident.categorize(createType(), IncidentPriority.A);

        assertStateChange(incident, IncidentState.READY_FOR_DISPATCH);
    }

    @Test(expectedExceptions = IncidentNotOpenException.class)
    public void categorize_incidentIsClosed_exceptionThrown() {
        final var incident = createIncident();
        incident.close(repositoryMock);
        incident.categorize(createType(), IncidentPriority.A);
    }

    @Test
    public void resourcesHaveBeenDispatched_incidentIsNew_noChange() {
        final var incident = createIncident();
        incident.resourcesHaveBeenDispatched();

        assertNew(incident);
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

    @Test(expectedExceptions = IncidentNotOpenException.class)
    public void resourcesHaveBeenDispatched_incidentIsClosed_exceptionThrown() {
        final var incident = createIncident();
        incident.close(repositoryMock);
        incident.resourcesHaveBeenDispatched();
    }

    @Test
    public void resourcesAreOnScene_incidentIsNew_noChange() {
        final var incident = createIncident();
        incident.resourcesAreOnScene();

        assertNew(incident);
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

    @Test(expectedExceptions = IncidentNotOpenException.class)
    public void resourcesAreOnScene_incidentIsClosed_exceptionThrown() {
        final var incident = createIncident();
        incident.close(repositoryMock);
        incident.resourcesAreOnScene();
    }

    @Test
    public void resourcesHaveClearedTheScene_incidentIsNew_nothingChanges() {
        final var incident = createIncident();
        incident.resourcesHaveClearedTheScene();

        assertNew(incident);
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

    @Test(expectedExceptions = IncidentNotOpenException.class)
    public void resourcesHaveClearedTheScene_incidentIsClosed_exceptionThrown() {
        final var incident = createIncident();
        incident.close(repositoryMock);
        incident.resourcesHaveClearedTheScene();
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

    @Test(expectedExceptions = IncidentNotOpenException.class)
    public void putOnHold_incidentIsClosed_exceptionThrown() {
        final var incident = createIncident();
        incident.close(repositoryMock);
        incident.putOnHold("reason");
    }

    @Test
    public void close_incidentIsNew_incidentIsClosed() {
        final var incident = createIncident();
        incident.close(repositoryMock);
        assertClosed(incident);
    }

    @Test
    public void close_incidentIsReadyForDispatch_incidentIsClosed() {
        final var incident = createIncident();
        incident.pinpoint(createLocation());
        incident.categorize(createType(), IncidentPriority.B);
        incident.close(repositoryMock);
        assertClosed(incident);
    }

    @Test(expectedExceptions = IncidentNotClearedException.class)
    public void close_incidentIsDispatched_exceptionThrown() {
        final var incident = createIncident();
        incident.pinpoint(createLocation());
        incident.categorize(createType(), IncidentPriority.B);
        incident.resourcesHaveBeenDispatched();
        incident.close(repositoryMock);
    }

    @Test(expectedExceptions = IncidentNotClearedException.class)
    public void close_resourcesAreOnScene_exceptionThrown() {
        final var incident = createIncident();
        incident.pinpoint(createLocation());
        incident.categorize(createType(), IncidentPriority.B);
        incident.resourcesHaveBeenDispatched();
        incident.resourcesAreOnScene();
        incident.close(repositoryMock);
    }

    @Test
    public void close_resourcesHaveClearedTheScene_incidentIsClosed() {
        final var incident = createIncident();
        incident.pinpoint(createLocation());
        incident.categorize(createType(), IncidentPriority.B);
        incident.resourcesHaveBeenDispatched();
        incident.resourcesAreOnScene();
        incident.resourcesHaveClearedTheScene();
        incident.close(repositoryMock);

        assertClosed(incident);
    }

    @Test
    public void close_incidentIsOnHold_incidentIsClosed() {
        final var incident = createIncident();
        incident.pinpoint(createLocation());
        incident.categorize(createType(), IncidentPriority.B);
        incident.putOnHold("reason");
        incident.close(repositoryMock);

        assertClosed(incident);
    }

    @Test
    public void close_incidentIsAlreadyClosed_nothingHappens() {
        final var incident = createIncident();
        incident.close(repositoryMock);
        clearDomainEvents(incident);
        incident.close(repositoryMock); // again

        assertNoPublishedDomainEvents(incident);
    }

    @Test
    public void updateDetails_incidentIsOpen_detailsAreUpdated() {
        final var incident = createIncident();
        incident.updateDetails("details");

        assertThat(incident.details()).contains("details");
        assertPublishedDomainEvent(incident, IncidentDetailsUpdatedEvent.class,
                event -> "details".equals(event.details()));
    }

    @Test
    public void updateDetails_sameAsExistingDetails_nothingHappens() {
        final var incident = createIncident();
        incident.updateDetails("details");
        clearDomainEvents(incident);
        incident.updateDetails("details"); // again

        assertNoPublishedDomainEvents(incident);
    }

    @Test(expectedExceptions = IncidentNotOpenException.class)
    public void updateDetails_incidentIsClosed_exceptionThrown() {
        final var incident = createIncident();
        incident.close(repositoryMock);
        incident.updateDetails("details");
    }

    @Test
    public void updateInformerDetails_incidentIsOpen_detailsAreUpdated() {
        final var incident = createIncident();
        final var name = "name";
        final var phoneNumber = createPhoneNumber();
        incident.updateInformerDetails(name, phoneNumber);

        assertThat(incident.informerName()).contains(name);
        assertThat(incident.informerPhoneNumber()).contains(phoneNumber);
        assertPublishedDomainEvent(incident, IncidentInformerDetailsUpdatedEvent.class, event ->
                name.equals(event.informerName().orElse(null))
                        && phoneNumber.equals(event.informerPhoneNumber().orElse(null)));
    }

    @Test
    public void updateInformerDetails_sameAsExistingDetails_nothingHappens() {
        final var incident = createIncident();
        final var name = "name";
        final var phoneNumber = createPhoneNumber();
        incident.updateInformerDetails(name, phoneNumber);
        clearDomainEvents(incident);
        incident.updateInformerDetails(name, phoneNumber); // again

        assertNoPublishedDomainEvents(incident);
    }

    @Test(expectedExceptions = IncidentNotOpenException.class)
    public void updateInformerDetails_incidentIsClosed_exceptionThrown() {
        final var incident = createIncident();
        incident.close(repositoryMock);
        incident.updateInformerDetails("name", createPhoneNumber());
    }

    @Test(expectedExceptions = UnsupportedOperationException.class)
    public void createSubIncident_notImplementedYet() {
        final var incident = createIncident();
        incident.createSubIncident(factoryMock);
    }

    private @NotNull Incident createIncident() {
        return new Incident(new IncidentId(UUID.randomUUID()));
    }

    private @NotNull Location createLocation() {
        throw new UnsupportedOperationException("Not implemented");
        //return new Location(new DirectPosition2D(60.286027, 60.286027), null, null) {
        //};
    }

    private @NotNull IncidentTypeId createType() {
        return new IncidentTypeId(UUID.randomUUID());
    }

    private @NotNull PhoneNumber createPhoneNumber() {
        return new PhoneNumber();
    }
}
