package net.pkhsolutions.idispatch.domain.resources;

import net.pkhsolutions.idispatch.domain.tickets.Ticket;

import java.util.List;
import java.util.Optional;

/**
 * Service interface for resource operations.
 */
public interface ResourceService {

    /**
     * Retrieves the current state of the specified resource (active or not).
     */
    ResourceStateChange getCurrentState(Resource resource);

    /**
     * Retrieves the current states of all active resources.
     */
    List<ResourceStateChange> getCurrentStatesOfActiveResources();

    /**
     * Retrieves the current states of all active resources that are assigned to the specified ticket.
     */
    List<ResourceStateChange> getCurrentStatesOfResourcesAssignedToTicket(Ticket ticket);

    /**
     * Retrieves all active resources.
     */
    List<Resource> getActiveResources();

    /**
     * Retrieves all resources that can currently be assigned to a ticket.
     */
    List<Resource> getAssignableResources();

    /**
     * Attempts to retrieve the resource with the specified call sign. If the call sign is {@code null} or empty,
     * or no such resource exists, an empty {@code Optional} is returned.
     */
    Optional<Resource> findByCallSign(String callSign);

    /**
     * Assigns the specified resource to the specified ticket, sets its state to {@link net.pkhsolutions.idispatch.domain.resources.ResourceState#ASSIGNED},
     * fires a {@link net.pkhsolutions.idispatch.domain.resources.events.ResourceStateChangedEvent} and returns true. If the resource is not assignable (i.e. it is assigned to another ticket),
     * this method does nothing and returns false. By setting the {@code force} parameter to true, an unassignable resource can be assigned to
     * the ticket anyway.
     */
    boolean assignResource(Resource resource, Ticket ticket, boolean force);

    /**
     * Removes the specified resource from its current ticket without changing its state and fires a {@link net.pkhsolutions.idispatch.domain.resources.events.ResourceStateChangedEvent}.
     * If the resource is not assigned to any ticket, this method does nothing.
     */
    void freeResource(Resource resource);

    /**
     * Sets the state of the specified resource to {@link net.pkhsolutions.idispatch.domain.resources.ResourceState#EN_ROUTE} and
     * fires a {@link net.pkhsolutions.idispatch.domain.resources.events.ResourceStateChangedEvent}. If the resource is
     * not assigned to any ticket, this method does nothing.
     */
    void resourceEnRoute(Resource resource);

    /**
     * Sets the state of the specified resource to {@link net.pkhsolutions.idispatch.domain.resources.ResourceState#ON_SCENE} and
     * fires a {@link net.pkhsolutions.idispatch.domain.resources.events.ResourceStateChangedEvent}. If the resource is
     * not assigned to any ticket, this method does nothing.
     */
    void resourceOnScene(Resource resource);

    /**
     * Sets the state of the specified resource to {@link net.pkhsolutions.idispatch.domain.resources.ResourceState#AVAILABLE} and
     * fires a {@link net.pkhsolutions.idispatch.domain.resources.events.ResourceStateChangedEvent}.
     */
    void resourceAvailable(Resource resource);

    /**
     * Sets the state of the specified resource to {@link net.pkhsolutions.idispatch.domain.resources.ResourceState#AT_STATION} and
     * fires a {@link net.pkhsolutions.idispatch.domain.resources.events.ResourceStateChangedEvent}.
     */
    void resourceAtStation(Resource resource);

    /**
     * Sets the state of the specified resource to {@link net.pkhsolutions.idispatch.domain.resources.ResourceState#UNAVAILABLE} and
     * fires a {@link net.pkhsolutions.idispatch.domain.resources.events.ResourceStateChangedEvent}.
     */
    void resourceUnavailable(Resource resource);
}
