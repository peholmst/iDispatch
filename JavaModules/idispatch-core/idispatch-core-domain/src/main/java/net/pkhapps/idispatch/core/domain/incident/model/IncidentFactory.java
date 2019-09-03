package net.pkhapps.idispatch.core.domain.incident.model;

import net.pkhapps.idispatch.core.domain.common.IdFactory;
import org.jetbrains.annotations.NotNull;

/**
 * TODO Document me
 */
@FunctionalInterface
public interface IncidentFactory {

    static @NotNull IncidentFactory createDefault(@NotNull IdFactory<IncidentId> idFactory) {
        return () -> new Incident(idFactory.createId());
    }

    @NotNull Incident createIncident();
}
