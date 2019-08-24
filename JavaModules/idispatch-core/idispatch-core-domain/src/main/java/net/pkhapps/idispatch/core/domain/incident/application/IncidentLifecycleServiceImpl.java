package net.pkhapps.idispatch.core.domain.incident.application;

import net.pkhapps.idispatch.core.domain.incident.model.*;
import org.jetbrains.annotations.NotNull;

import static java.util.Objects.requireNonNull;

/**
 * Implementation of {@link IncidentLifecycleService}.
 */
class IncidentLifecycleServiceImpl implements IncidentLifecycleService {

    private final IncidentFactory incidentFactory;
    private final IncidentRepository incidentRepository;

    IncidentLifecycleServiceImpl(@NotNull IncidentFactory incidentFactory,
                                 @NotNull IncidentRepository incidentRepository) {
        this.incidentFactory = requireNonNull(incidentFactory);
        this.incidentRepository = requireNonNull(incidentRepository);
    }

    @Override
    public @NotNull IncidentId openIncident() {
        final var incident = incidentFactory.createIncident();
        incidentRepository.save(incident);
        return incident.id();
    }

    @Override
    public @NotNull IncidentId openSubIncident(@NotNull IncidentId parentIncident) throws IncidentNotKnownException,
            IncidentNotOpenException {
        final var parent = incidentRepository.findById(parentIncident).orElseThrow(IncidentNotKnownException::new);
        final var incident = parent.createSubIncident(incidentFactory);
        incidentRepository.save(incident);
        return incident.id();
    }

    @Override
    public void putIncidentOnHold(@NotNull IncidentId incident, @NotNull String reason) throws IncidentNotKnownException, IncidentNotOpenException, InsufficientIncidentInformationException {

    }

    @Override
    public void closeIncident(@NotNull IncidentId incident) throws IncidentNotKnownException, IncidentNotClearedException, IncidentNotOpenException {

    }
}
