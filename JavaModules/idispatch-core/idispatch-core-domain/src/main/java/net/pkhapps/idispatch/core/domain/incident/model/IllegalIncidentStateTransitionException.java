package net.pkhapps.idispatch.core.domain.incident.model;

/**
 * Exception thrown when trying to transition from one {@link IncidentState} to another that is not allowed.
 */
public class IllegalIncidentStateTransitionException extends IllegalStateException {
}
