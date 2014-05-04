package net.pkhsolutions.idispatch.domain.resources;

import net.pkhsolutions.idispatch.domain.resources.events.ResourceStatusChangedEvent;
import net.pkhsolutions.idispatch.domain.tickets.Ticket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Persistable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
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
    EntityManager entityManager;

    @SuppressWarnings("unchecked")
    private <T extends Persistable<?>> T reattach(T detached) {
        if (entityManager.contains(detached)) {
            return detached;
        } else {
            return (T) entityManager.getReference(detached.getClass(), detached.getId());
        }
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = true)
    public ResourceStatus getCurrentStatus(Resource resource) {
        logger.debug("Looking up current status of resource {}", resource);
        final ResourceStatus status = resourceStatusRepository.findByResource(checkNotNull(resource));
        if (status == null) {
            logger.debug("No status information stored for resource {}", resource);
            return new ResourceStatus(resource, ResourceState.UNAVAILABLE);
        } else {
            return status;
        }
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = true)
    public List<ResourceStatus> getCurrentStatusOfActiveResources() {
        logger.debug("Looking up current status of all active resources");
        // TODO this method could be implemented with less queries
        return resourceRepository.findByActiveTrueOrderByCallSignAsc().stream().map(resource -> getCurrentStatus(resource)).collect(Collectors.toList());
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = true)
    public List<ResourceStatus> getCurrentStatusOfResourcesAssignedToTicket(Ticket ticket) {
        return resourceStatusRepository.findByTicket(checkNotNull(ticket));
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = true)
    public List<ResourceStatus> getCurrentStatusOfActiveAssignableResources() {
        return resourceStatusRepository.findActiveAssignableResources();
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public boolean assignResource(Resource resource, Ticket ticket, boolean force) {
        resource = reattach(checkNotNull(resource));
        ticket = reattach(checkNotNull(ticket));

        logger.debug("Attempting to assign resource {} to ticket {}", resource, ticket);
        final ResourceStatus status = getCurrentStatus(resource);
        if (status.getTicket() != null && !force) {
            logger.debug("Resource {} is already assigned to another ticket and force is false", resource);
            return false;
        }
        status.setState(ResourceState.ASSIGNED);
        status.setTicket(ticket);
        save(status);
        return true;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void freeResource(Resource resource) {
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
    @Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = true)
    public Set<ResourceState> getPossibleResourceStateTransitions(Resource resource) {
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
        }
        return emptySet();
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void setResourceState(Resource resource, ResourceState state) {
        resource = reattach(checkNotNull(resource));
        if (getPossibleResourceStateTransitions(resource).contains(state)) {
            final ResourceStatus status = getCurrentStatus(resource);
            status.setState(checkNotNull(state));
            save(status);
        }
    }

    private void save(ResourceStatus resourceStatus) {
        final ResourceStatus updated = resourceStatusRepository.saveAndFlush(resourceStatus);
        archivedResourceStatusRepository.saveAndFlush(updated.toArchived());
        eventPublisher.publishEvent(new ResourceStatusChangedEvent(this, updated));
    }
}
