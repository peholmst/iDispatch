package net.pkhapps.idispatch.core.domain.incident.application;

/**
 * Exception thrown when trying to perform an operation on a closed incident.
 */
public class IncidentNotOpenException extends IllegalStateException {
}
