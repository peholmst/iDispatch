package net.pkhsolutions.idispatch.boundary;

import net.pkhsolutions.idispatch.entity.Assignment;
import net.pkhsolutions.idispatch.entity.Resource;
import net.pkhsolutions.idispatch.entity.ResourceState;
import net.pkhsolutions.idispatch.entity.ResourceStatus;

import java.util.List;
import java.util.Optional;

/**
 * Service interface for resource status operations.
 */
public interface ResourceStatusService {

    /**
     * Finds the current status of the specified resource. If the resource is {@code null}, not active, or it
     * does not exist, an empty {@code Optional} is returned.
     *
     * @see net.pkhsolutions.idispatch.entity.Resource#isActive()
     */
    Optional<ResourceStatus> findCurrentStatus(Resource resource);

    /**
     * Attempts to set the state of the specified resource. If the requested state transition is not valid or the resource is not active,
     * nothing happens and false is returned. Otherwise, a {@link net.pkhsolutions.idispatch.boundary.events.ResourceStatusChanged} event is fired and true is returned.
     *
     * @see net.pkhsolutions.idispatch.entity.ResourceStatus#getAllValidNextStates()
     * @see net.pkhsolutions.idispatch.entity.Resource#isActive()
     */
    boolean setResourceState(Resource resource, ResourceState resourceState);

    /**
     * Attempts to assign the resource to the specified assignment. If the resource is not active or not available, nothing
     * happens and false is returned. Otherwise, a {@link net.pkhsolutions.idispatch.boundary.events.ResourceStatusChanged} event is fired and true is returned.
     *
     * @see net.pkhsolutions.idispatch.entity.ResourceStatus#isAvailable()
     * @see net.pkhsolutions.idispatch.entity.Resource#isActive()
     */
    boolean setResourceAssignment(Resource resource, Assignment assignment);

    /**
     * Attempts to clear the assignment from the specified resource, fires a {@link net.pkhsolutions.idispatch.boundary.events.ResourceStatusChanged} event and returns true if successful.
     * This can only happen if the resource is active and is in any of the following states:
     * <ul>
     * <li>{@link ResourceState#RESERVED}, in which case the state of the resource will change to whatever it was before the resource was reserved)</li>
     * <li>{@link ResourceState#AVAILABLE}</li>
     * <li>{@link ResourceState#AT_STATION}</li>
     * <li>{@link ResourceState#OUT_OF_SERVICE}</li>
     * </ul>
     * Otherwise, nothing will happen and false is returned.
     *
     * @see net.pkhsolutions.idispatch.entity.Resource#isActive()
     */
    boolean clearResourceAssignment(Resource resource);

    /**
     * Finds all resources that are currently assigned to the specified assignment.
     *
     * @see net.pkhsolutions.idispatch.entity.ResourceStatus#isAssigned()
     */
    List<Resource> getResourcesAssignedToAssignment(Assignment assignment);

    /**
     * Finds the current status of all resources that are currently assigned to the specified assignment.
     *
     * @see net.pkhsolutions.idispatch.entity.ResourceStatus#isAssigned()
     */
    List<ResourceStatus> getStatusOfResourcesAssignedToAssignment(Assignment assignment);

    /**
     * Finds all active and available resources.
     *
     * @see net.pkhsolutions.idispatch.entity.Resource#isActive()
     * @see net.pkhsolutions.idispatch.entity.ResourceStatus#isAvailable()
     */
    List<Resource> getAvailableResources();

    /**
     * Finds the current status of all active and available resources.
     *
     * @see net.pkhsolutions.idispatch.entity.Resource#isActive()
     * @see net.pkhsolutions.idispatch.entity.ResourceStatus#isAvailable()
     */
    List<ResourceStatus> getStatusOfAvailableResources();

    /**
     * Finds all active resources.
     *
     * @see net.pkhsolutions.idispatch.entity.Resource#isActive()
     */
    List<Resource> getAllResources();

    /**
     * Finds the current status of all active resources.
     *
     * @see net.pkhsolutions.idispatch.entity.Resource#isActive()
     */
    List<ResourceStatus> getStatusOfAllResources();
}
