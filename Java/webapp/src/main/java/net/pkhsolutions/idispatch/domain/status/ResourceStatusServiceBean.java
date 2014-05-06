package net.pkhsolutions.idispatch.domain.status;

import net.pkhsolutions.idispatch.domain.resources.Resource;
import net.pkhsolutions.idispatch.domain.status.events.ResourceStatusChangedEvent;
import net.pkhsolutions.idispatch.domain.tickets.Ticket;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.support.TransactionTemplate;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ResourceStatusServiceBean implements ResourceStatusService {

    @Autowired
    ResourceStatusRepository resourceStatusRepository;
    @Autowired
    ArchivedResourceStatusRepository archivedResourceStatusRepository;
    @Autowired
    ApplicationContext appContext;
    @Autowired
    PlatformTransactionManager transactionManager;

    private TransactionTemplate transactionTemplate;

    @PostConstruct
    void init() {
        transactionTemplate = new TransactionTemplate(transactionManager);
        transactionTemplate.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
        transactionTemplate.setName("ResourceStatusTX");
    }

    @Override
    public ResourceStatus getCurrentStatus(Resource resource) {
        final ResourceStatus resourceStatus = resourceStatusRepository.findByResource(resource);
        if (resourceStatus == null) {
            return transactionTemplate.execute(status -> resourceStatusRepository.saveAndFlush(new ResourceStatus(resource, ResourceState.UNAVAILABLE)));
        } else {
            return resourceStatus;
        }
    }

    @Override
    public boolean setResourceState(Resource resource, ResourceState resourceState) {
        ResourceStatus currentStatus = getCurrentStatus(resource);
        if (currentStatus.getAllValidNextStates().contains(resourceState)) {
            currentStatus.setState(resourceState);
            save(currentStatus);
            return true;
        }
        return false;
    }

    @Override
    public boolean assignToTicket(Resource resource, Ticket ticket) {
        final ResourceStatus currentStatus = getCurrentStatus(resource);
        if (currentStatus.isAvailable()) {
            currentStatus.setState(ResourceState.ASSIGNED);
            currentStatus.setTicket(ticket);
            save(currentStatus);
            return true;
        }
        return false;
    }

    @Override
    public void removeTicket(Resource resource) {
        // TODO Implement me!
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @Override
    public List<Resource> getResourcesAssignedToTicket(Ticket ticket) {
        return getStatusOfResourcesAssignedToTicket(ticket).stream().map(ResourceStatus::getResource).collect(Collectors.toList());
    }

    @Override
    public List<ResourceStatus> getStatusOfResourcesAssignedToTicket(Ticket ticket) {
        return resourceStatusRepository.findByTicketAndDetachedFalse(ticket);
    }

    @Override
    public List<Resource> getAvailableResources() {
        return getStatusOfAvailableResources().stream().map(ResourceStatus::getResource).collect(Collectors.toList());
    }

    @Override
    public List<ResourceStatus> getStatusOfAvailableResources() {
        return resourceStatusRepository.findByAvailableTrue();
    }

    private void save(ResourceStatus resourceStatus) {
        final ResourceStatus updated = transactionTemplate.execute(status -> {
            archivedResourceStatusRepository.saveAndFlush(resourceStatus.toArchived());
            return resourceStatusRepository.saveAndFlush(resourceStatus);
        });
        appContext.publishEvent(new ResourceStatusChangedEvent(this, updated));
    }
}
