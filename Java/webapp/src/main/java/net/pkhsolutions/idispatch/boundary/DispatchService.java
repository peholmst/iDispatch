package net.pkhsolutions.idispatch.boundary;

import net.pkhsolutions.idispatch.entity.*;

import java.util.Collection;
import java.util.List;

/**
 * Service interface for dispatch operations.
 */
public interface DispatchService {

    /**
     * Dispatches the selected resources to the assignment and fires a {@link net.pkhsolutions.idispatch.boundary.events.DispatchNotificationSent} event.
     * Resources in the {@link net.pkhsolutions.idispatch.entity.ResourceState#RESERVED} state will change into the {@link net.pkhsolutions.idispatch.entity.ResourceState#DISPATCHED} state,
     * any other resources will remain in their previous states. Any resources not assigned to the assignment will be ignored.
     * <p>
     * Callers can listen for {@link net.pkhsolutions.idispatch.boundary.events.DispatchNotificationReceived} events to check whether the
     * dispatch notification reached its destinations.
     *
     * @throws net.pkhsolutions.idispatch.entity.ValidationFailedException if the assignment did not contain enough information
     */
    DispatchNotification dispatchSelectedResources(Assignment assignment, Collection<Resource> resources) throws ValidationFailedException;

    /**
     * Same as calling {@link #dispatchSelectedResources(net.pkhsolutions.idispatch.entity.Assignment, java.util.Collection)},
     * but passing in all assigned resources.
     *
     * @throws net.pkhsolutions.idispatch.entity.ValidationFailedException if the assignment did not contain enough information
     * @see net.pkhsolutions.idispatch.entity.ResourceStatus#isAssigned()
     */
    DispatchNotification dispatchAllResources(Assignment assignment) throws ValidationFailedException;

    /**
     * Same as calling {@link #dispatchSelectedResources(net.pkhsolutions.idispatch.entity.Assignment, java.util.Collection)}},
     * but passing in all assigned resources that are in the {@link net.pkhsolutions.idispatch.entity.ResourceState#RESERVED} state.
     *
     * @throws net.pkhsolutions.idispatch.entity.ValidationFailedException if the assignment did not contain enough information
     * @see net.pkhsolutions.idispatch.entity.ResourceStatus#isAssigned()
     */
    DispatchNotification dispatchAllReservedResources(Assignment assignment) throws ValidationFailedException;

    /**
     * Finds all dispatch notifications for the specified assignment.
     */
    List<DispatchNotification> findDispatchNotificationsForAssignment(Assignment assignment);

    /**
     * Finds all receipts for the specified dispatch notification.
     */
    List<Receipt> findReceiptsForDispatchNotification(DispatchNotification dispatchNotification);
}
