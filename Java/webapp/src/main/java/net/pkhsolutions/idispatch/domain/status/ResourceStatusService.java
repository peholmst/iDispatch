package net.pkhsolutions.idispatch.domain.status;

import net.pkhsolutions.idispatch.domain.resources.Resource;
import net.pkhsolutions.idispatch.domain.tickets.Ticket;

import java.util.List;

public interface ResourceStatusService {

    ResourceStatus getCurrentStatus(Resource resource);

    boolean setResourceState(Resource resource, ResourceState resourceState);

    boolean assignToTicket(Resource resource, Ticket ticket);

    void removeTicket(Resource resource);

    List<Resource> getResourcesAssignedToTicket(Ticket ticket);

    List<ResourceStatus> getStatusOfResourcesAssignedToTicket(Ticket ticket);

    List<Resource> getAvailableResources();

    List<ResourceStatus> getStatusOfAvailableResources();
}
