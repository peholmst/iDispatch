package net.pkhapps.idispatch.core.domain.incident.model;

import net.pkhapps.idispatch.core.domain.common.IdFactory;
import org.jetbrains.annotations.NotNull;

import static java.util.Objects.requireNonNull;

/**
 * TODO Document me
 */
public class DefaultIncidentFactory implements IncidentFactory {

    private final IdFactory<IncidentId> idFactory;

    public DefaultIncidentFactory() {
        this(IncidentId::random);
    }

    public DefaultIncidentFactory(@NotNull IdFactory<IncidentId> idFactory) {
        this.idFactory = requireNonNull(idFactory);
    }

    @Override
    public @NotNull Incident createIncident() {
        return new Incident(idFactory.createId());
    }
}
