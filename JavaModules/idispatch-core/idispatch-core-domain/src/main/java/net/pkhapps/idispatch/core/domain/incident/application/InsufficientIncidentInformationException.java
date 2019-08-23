package net.pkhapps.idispatch.core.domain.incident.application;

/**
 * Exception thrown when attempting to perform an operation on an incident before the incident contains sufficient
 * information to perform the operation in question.
 */
public class InsufficientIncidentInformationException extends IllegalStateException {
}
