package net.pkhapps.idispatch.core.domain.incident.model;

import org.jetbrains.annotations.NotNull;

/**
 * TODO Implement me!
 */
@FunctionalInterface
public interface IncidentFactory {

    @NotNull Incident createIncident();
}
