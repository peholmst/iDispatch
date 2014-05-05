package net.pkhsolutions.idispatch.domain.resources;

import net.pkhsolutions.idispatch.domain.resources.events.ResourceStatusChangedEvent;
import net.pkhsolutions.idispatch.domain.tickets.Ticket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.support.TransactionTemplate;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.collect.Sets.newHashSet;
import static java.util.Collections.emptySet;

/**
 * Implementation of {@link net.pkhsolutions.idispatch.domain.resources.ResourceService}.
 */
@Service
public class ResourceServiceBean implements ResourceService {

    // TODO several of the methods in this bean could be implemented with less queries

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    ResourceRepository resourceRepository;
    @Autowired
    ResourceStatusRepository resourceStatusRepository;
    @Autowired
    ArchivedResourceStatusRepository archivedResourceStatusRepository;
    @Autowired
    ApplicationEventPublisher eventPublisher;
    @Autowired
    PlatformTransactionManager transactionManager;
    private TransactionTemplate transactionTemplate;

    @PostConstruct
    void init() {
        transactionTemplate = new TransactionTemplate(transactionManager);
        transactionTemplate.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
        transactionTemplate.setName("ResourceStatusSavingTX");
    }

    @Override
    public ResourceStatus getCurrentStatus(Resource resource) {
        logger.debug("Looking up current status of resource {}", resource);
        final ResourceStatus resourceStatus = resourceStatusRepository.findByResource(checkNotNull(resource));
        if (resourceStatus == null) {
            logger.debug("No status information stored for resource {}", resource);
            return transactionTemplate.execute(status -> resourceStatusRepository.saveAndFlush(new ResourceStatus(resource, ResourceState.UNAVAILABLE)));
        } else {
            return resourceStatus;
        }
    }

    @Override
    public List<ResourceStatus> getCurrentStatusOfActiveResources() {
        logger.debug("Looking up current status of all active resources");
        return resourceRepository.findByActiveTrueOrderByCallSignAsc().stream().map(resource -> getCurrentStatus(resource)).collect(Collectors.toList());
    }

    @Override
    public List<ResourceStatus> getCurrentStatusOfResourcesAssignedToTicket(Ticket ticket) {
        logger.debug("Looking up current status of resources assigned to ticket {}", ticket);
        return resourceStatusRepository.findByTicket(checkNotNull(ticket));
    }

    @Override
    public List<ResourceStatus> getCurrentStatusOfActiveAssignableResources() {
        logger.debug("Looking up current status of active assignable resources");
        return resourceStatusRepository.findActiveResourcesInStates(newHashSet(ResourceState.AT_STATION, ResourceState.AVAILABLE));
    }

    @Override
    public boolean assignResource(Resource resource, Ticket ticket, boolean force) {
        logger.debug("Attempting to assign resource {} to ticket {}", resource, ticket);
        final ResourceStatus status = getCurrentStatus(resource);
        if (status.getTicket() != null && !force) {
            logger.debug("Resource {} is already assigned to another ticket and force is false", resource);
            return false;
        }
        status.setState(ResourceState.ASSIGNED);
        status.setTicket(checkNotNull(ticket));
        save(status);
        return true;
    }

    @Override
    public void freeResource(Resource resource) {
        // TODO Implement me!
        throw new UnsupportedOperationException("Not implemented yet");
/*
        final AbstractResourceStateChange currentState = getCurrentStatus(resource);
        if (currentState.getTicket() != null) {
            // TODO This implementation is wrong
            logger.debug("Freeing resource {} from ticket {} without changing the state", resource, currentState.getTicket());
            save(new AbstractResourceStateChange.Builder(currentState).withTicket(null).build());
        }*/
    }

    @Override
    public Set<ResourceState> getPossibleResourceStateTransitions(Resource resource) {
        logger.debug("Calculating possible resource state transitions for resource {}", resource);
        final ResourceStatus status = getCurrentStatus(resource);
        switch (status.getState()) {
            case AVAILABLE:
                return newHashSet(ResourceState.AT_STATION, ResourceState.UNAVAILABLE);
            case AT_STATION:
                return newHashSet(ResourceState.AVAILABLE, ResourceState.UNAVAILABLE);
            case DISPATCHED:
                return newHashSet(ResourceState.EN_ROUTE, ResourceState.AVAILABLE, ResourceState.AT_STATION);
            case EN_ROUTE:
                return newHashSet(ResourceState.ON_SCENE, ResourceState.AVAILABLE, ResourceState.AT_STATION);
            case ON_SCENE:
                return newHashSet(ResourceState.AVAILABLE, ResourceState.UNAVAILABLE, ResourceState.AT_STATION);
            case ASSIGNED:
                return newHashSet(ResourceState.EN_ROUTE, ResourceState.ON_SCENE);
            case UNAVAILABLE:
                return newHashSet(ResourceState.AT_STATION, ResourceState.AVAILABLE);
        }
        return emptySet();
    }

    @Override
    public void setResourceState(Resource resource, ResourceState state) {
        logger.debug("Attempting to set state of resource {} to {}", resource, state);
        if (getPossibleResourceStateTransitions(resource).contains(state)) {
            final ResourceStatus status = getCurrentStatus(resource);
            status.setState(checkNotNull(state));
            save(status);
        }
    }

    private void save(ResourceStatus resourceStatus) {
        logger.debug("Saving {}", resourceStatus);
        final ResourceStatus updated = transactionTemplate.execute(status -> {
            archivedResourceStatusRepository.saveAndFlush(resourceStatus.toArchived());
            return resourceStatusRepository.saveAndFlush(resourceStatus);
        });
        eventPublisher.publishEvent(new ResourceStatusChangedEvent(this, updated));
    }
}
