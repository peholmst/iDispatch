package net.pkhapps.idispatch.alert.server.application.dispatcher;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import java.time.Instant;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import net.pkhapps.idispatch.alert.server.application.security.AccessDeniedException;
import net.pkhapps.idispatch.alert.server.application.security.MockSecurityManager;
import net.pkhapps.idispatch.alert.server.domain.infra.MockUnitOfWorkManager;
import net.pkhapps.idispatch.alert.server.domain.model.AlertId;
import net.pkhapps.idispatch.alert.server.domain.model.GeoPoint;
import net.pkhapps.idispatch.alert.server.domain.model.IncidentTypeCode;
import net.pkhapps.idispatch.alert.server.domain.model.IncidentUrgencyCode;
import net.pkhapps.idispatch.alert.server.domain.model.Municipality;
import net.pkhapps.idispatch.alert.server.domain.model.ResourceIdentifier;

class DispatcherPortImplTest {

    private MockUnitOfWorkManager unitOfWorkManager;
    private MockSecurityManager securityManager;
    private AlertCommandHandler alertCommandHandler;
    private AlertStatusHandler alertStatusHandler;
    private DispatcherPort dispatcherPort;

    @BeforeEach
    void setUp() {
        unitOfWorkManager = new MockUnitOfWorkManager();
        securityManager = new MockSecurityManager();
        alertCommandHandler = mock(AlertCommandHandler.class);
        alertStatusHandler = mock(AlertStatusHandler.class);
        dispatcherPort = new DispatcherPortImpl(unitOfWorkManager, securityManager, alertCommandHandler,
                alertStatusHandler);
    }

    @Test
    void alert_userHasCorrectPermission_accessGranted() {
        var command = newCommand();
        var alertId = AlertId.randomAlertId();
        when(alertCommandHandler.alert(command)).thenReturn(alertId);
        securityManager.setPermissionsOfCurrentUser(DispatcherPermission.SEND_ALERT);
        assertThat(dispatcherPort.alert(command)).isEqualTo(alertId);
        assertThat(unitOfWorkManager.<AlertId>getLastUnitOfWorkResult()).isEqualTo(alertId);
    }

    @Test
    void alert_userHasNoPermission_accessDenied() {
        var command = newCommand();
        assertThatThrownBy(() -> dispatcherPort.alert(command)).isInstanceOf(AccessDeniedException.class);
        verifyNoInteractions(alertCommandHandler);
    }

    @Test
    void getStatus_userHasCorrectPermission_accessGranted() {
        var alertId = AlertId.randomAlertId();
        var status = newStatus();
        when(alertStatusHandler.getStatus(alertId)).thenReturn(Optional.of(status));
        securityManager.setPermissionsOfCurrentUser(DispatcherPermission.CHECK_ALERT_STATUS);
        assertThat(dispatcherPort.getStatus(alertId)).contains(status);
        assertThat(unitOfWorkManager.<Optional<AlertStatus>>getLastUnitOfWorkResult()).contains(status);
    }

    @Test
    void getStatus_userHasNoPermission_accessDenied() {
        var alertId = AlertId.randomAlertId();
        assertThatThrownBy(() -> dispatcherPort.getStatus(alertId)).isInstanceOf(AccessDeniedException.class);
        verifyNoInteractions(alertStatusHandler);
    }

    private static AlertCommand newCommand() {
        return AlertCommand.builder().withIncidentType(IncidentTypeCode.fromString("401"))
                .withIncidentUrgency(IncidentUrgencyCode.fromString("B"))
                .withMunicipality(Municipality.fromMonolingualName("Municipality"))
                .withCoordinates(GeoPoint.fromLatLon(60, 22))
                .withResourceToAlert(ResourceIdentifier.fromString("RVS911")).build();
    }

    private static AlertStatus newStatus() {
        return AlertStatus.builder().withAlertId(AlertId.randomAlertId())
                .withAlertedResource(ResourceIdentifier.fromString("RVS911"), Instant.now()).build();
    }
}
