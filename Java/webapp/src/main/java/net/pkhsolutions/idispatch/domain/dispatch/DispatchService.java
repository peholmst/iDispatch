package net.pkhsolutions.idispatch.domain.dispatch;

import net.pkhsolutions.idispatch.domain.resources.Resource;
import net.pkhsolutions.idispatch.domain.tickets.Ticket;

import java.util.Collection;

public interface DispatchService {

    DispatchNotification dispatchSelectedResources(Ticket ticket, Collection<Resource> resources);

    DispatchNotification dispatchAllAssignedResources(Ticket ticket);

    DispatchNotification dispatchAllUndispatchedResources(Ticket ticket);
}
