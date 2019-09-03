package net.pkhapps.idispatch.core.domain.incident.application;

import net.pkhapps.idispatch.core.domain.incident.model.IncidentFactory;
import net.pkhapps.idispatch.core.domain.incident.model.IncidentId;
import net.pkhapps.idispatch.core.domain.incident.model.IncidentRepository;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

/**
 * Unit test for {@link IncidentLifecycleServiceImpl}.
 */
public class IncidentLifecycleServiceImplTest {

    private IncidentLifecycleService lifecycleService;
    private IncidentFactory incidentFactory;
    private IncidentFactory incidentFactoryMock;
    private IncidentRepository incidentRepositoryMock;

    @BeforeTest
    public void setUpTest() {
        incidentFactory = IncidentFactory.createDefault(() -> new IncidentId(UUID.randomUUID()));
        incidentFactoryMock = mock(IncidentFactory.class);
        incidentRepositoryMock = mock(IncidentRepository.class);
        lifecycleService = new IncidentLifecycleServiceImpl(incidentFactoryMock, incidentRepositoryMock);
    }

    @BeforeMethod
    public void setUpTestMethod() {
        reset(incidentFactoryMock, incidentRepositoryMock);
    }

    @Test
    public void openIncident_incidentHasIdAndIsSaved() {
        var incident = incidentFactory.createIncident();
        when(incidentFactoryMock.createIncident()).thenReturn(incident);

        var openedIncidentId = lifecycleService.openIncident();

        assertThat(openedIncidentId).isEqualTo(incident.id());
        verify(incidentRepositoryMock).save(incident);
    }

    @Test
    public void openSubIncident_parentExistsAndIsOpen_subIncidentHasIdAndIsSaved() {
        var parent = incidentFactory.createIncident();
        when(incidentRepositoryMock.findById(parent.id())).thenReturn(Optional.of(parent));
        var child = incidentFactory.createIncident();
        when(incidentFactoryMock.createIncident()).thenReturn(child);

        var openedIncidentId = lifecycleService.openSubIncident(parent.id());

        assertThat(openedIncidentId).isEqualTo(child.id());
        assertThat(child.parent()).contains(parent.id());
        verify(incidentRepositoryMock).save(child);
    }

    @Test(expectedExceptions = IncidentNotKnownException.class)
    public void openSubIncident_parentDoesNotExist_exceptionThrown() {
        var parentId = new IncidentId(UUID.randomUUID());
        when(incidentRepositoryMock.findById(parentId)).thenReturn(Optional.empty());
        verifyNoMoreInteractions(incidentRepositoryMock);
        verifyNoMoreInteractions(incidentFactoryMock);

        lifecycleService.openSubIncident(parentId);
    }

/*    @Test(expectedExceptions = IncidentNotOpenException.class)
    public void openSubIncident_parentIsClosed_exceptionThrown() {
        var parentId = new IncidentId();
        var parent = new Incident(parentId);
        parent.close();
        when(incidentRepositoryMock.findById(parentId)).thenReturn(Optional.of(parent));
        verifyNoMoreInteractions(incidentRepositoryMock);

        lifecycleService.openSubIncident(parentId);
    }*/
}
