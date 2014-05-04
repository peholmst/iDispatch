package net.pkhsolutions.idispatch.domain.resources;

import net.pkhsolutions.idispatch.domain.tickets.Ticket;

import java.util.List;
import java.util.Set;

/**
 * Service interface for resource operations.
 */
public interface ResourceService {

    /**
     * Retrieves the current status of the specified resource (active or not).
     */
    ResourceStatus getCurrentStatus(Resource resource);

    /**
     * Retrieves all the states that the specified resource currently can transition into without assigning or freeing the resource.
     */
    Set<ResourceState> getPossibleResourceStateTransitions(Resource resource);

    /**
     * Sets the state of the specified resource, firing a {@link net.pkhsolutions.idispatch.domain.resources.events.ResourceStatusChangedEvent}.
     * If the resource cannot transition into the state, nothing happens.
     */
    void setResourceState(Resource resource, ResourceState state);

    /**
     * Retrieves the current status of all active resources.
     */
    List<ResourceStatus> getCurrentStatusOfActiveResources();

    /**
     * Retrieves the current status of all resources currently assigned to the specified ticket.
     */
    List<ResourceStatus> getCurrentStatusOfResourcesAssignedToTicket(Ticket ticket);

    /**
     * Retrieves the current status of all active resources that are free to be assigned to a ticket.
     */
    List<ResourceStatus> getCurrentStatusOfActiveAssignableResources();

    /**
     * Assigns the specified resource to the specified ticket, sets its state to {@link net.pkhsolutions.idispatch.domain.resources.ResourceState#ASSIGNED},
     * fires a {@link net.pkhsolutions.idispatch.domain.resources.events.ResourceStatusChangedEvent} and returns true. If the resource is not assignable (i.e. it is assigned to another ticket),
     * or the ticket is closed, this method does nothing and returns false. By setting the {@code force} parameter to true,
     * an unassignable resource can be assigned to the ticket anyway. The {@code force} parameter will not have any effect if the ticket is closed, though.
     */
    boolean assignResource(Resource resource, Ticket ticket, boolean force);

    /**
     * Removes the specified resource from its current ticket, changes its state to what it was just before the resource was assigned and fires a
     * {@link net.pkhsolutions.idispatch.domain.resources.events.ResourceStatusChangedEvent}. If the resource is not assigned to any ticket, this method does nothing.
     */
    void freeResource(Resource resource);

}
