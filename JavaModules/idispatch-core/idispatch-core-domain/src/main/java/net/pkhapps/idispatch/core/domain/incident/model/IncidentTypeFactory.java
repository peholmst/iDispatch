package net.pkhapps.idispatch.core.domain.incident.model;

import net.pkhapps.idispatch.core.domain.common.IdFactory;
import org.jetbrains.annotations.NotNull;

/**
 * TODO Document me
 */
@FunctionalInterface
public interface IncidentTypeFactory {

    static @NotNull IncidentTypeFactory createDefault(@NotNull IdFactory<IncidentTypeId> idFactory) {
        return () -> new IncidentType(idFactory.createId());
    }

    @NotNull IncidentType createIncidentType();
}
