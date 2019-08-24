package net.pkhapps.idispatch.core.domain.incident.model;

/**
 * Exception thrown when trying to close an incident that has not been cleared yet.
 */
public class IncidentNotClearedException extends IllegalStateException {
}