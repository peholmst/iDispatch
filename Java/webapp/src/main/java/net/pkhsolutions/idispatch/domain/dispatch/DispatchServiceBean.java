package net.pkhsolutions.idispatch.domain.dispatch;

import net.pkhsolutions.idispatch.domain.dispatch.event.DispatchNotificationSent;
import net.pkhsolutions.idispatch.domain.resources.Resource;
import net.pkhsolutions.idispatch.domain.status.ResourceState;
import net.pkhsolutions.idispatch.domain.status.ResourceStatus;
import net.pkhsolutions.idispatch.domain.status.ResourceStatusService;
import net.pkhsolutions.idispatch.domain.tickets.Ticket;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;

import javax.annotation.PostConstruct;
import java.util.Collection;
import java.util.stream.Collectors;

@Service
public class DispatchServiceBean implements DispatchService {

    @Autowired
    ApplicationContext applicationContext;
    @Autowired
    DispatchNotificationRepository dispatchNotificationRepository;
    @Autowired
    DestinationRepository destinationRepository;
    @Autowired
    PlatformTransactionManager txManager;
    @Autowired
    ResourceStatusService resourceStatusService;

    private TransactionTemplate transactionTemplate;

    @PostConstruct
    void init() {
        transactionTemplate = new TransactionTemplate(txManager);
    }

    @Override
    public DispatchNotification dispatchSelectedResources(Ticket ticket, Collection<Resource> resources) {
        final DispatchNotification notification = transactionTemplate.execute(status ->
                        dispatchNotificationRepository.saveAndFlush(createNotification(ticket, resources))
        );
        applicationContext.publishEvent(new DispatchNotificationSent(this, notification));
        resources.forEach(resource -> resourceStatusService.setResourceState(resource, ResourceState.DISPATCHED));
        return notification;
    }

    @Override
    public DispatchNotification dispatchAllAssignedResources(Ticket ticket) {
        return dispatchSelectedResources(ticket, resourceStatusService.getResourcesAssignedToTicket(ticket));
    }

    @Override
    public DispatchNotification dispatchAllUndispatchedResources(Ticket ticket) {
        return dispatchSelectedResources(ticket, resourceStatusService.getStatusOfResourcesAssignedToTicket(ticket).stream()
                .filter(status -> status.getState() == ResourceState.ASSIGNED)
                .map(ResourceStatus::getResource)
                .collect(Collectors.toList()));
    }

    private DispatchNotification createNotification(Ticket ticket, Collection<Resource> resources) {
        return new DispatchNotification(ticket,
                findAssignedResources(ticket),
                findDestinations(resources));
    }

    private Collection<Destination> findDestinations(Collection<Resource> resources) {
        return resources.stream()
                .flatMap(resource -> destinationRepository.findDestinationsForResource(resource).stream())
                .collect(Collectors.toSet());
    }

    private Collection<Resource> findAssignedResources(Ticket ticket) {
        return resourceStatusService.getResourcesAssignedToTicket(ticket);
    }
}
