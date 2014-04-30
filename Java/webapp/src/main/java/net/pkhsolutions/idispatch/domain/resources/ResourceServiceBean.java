package net.pkhsolutions.idispatch.domain.resources;

import net.pkhsolutions.idispatch.domain.resources.events.ResourceStateChangedEvent;
import net.pkhsolutions.idispatch.domain.tickets.Ticket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Strings.isNullOrEmpty;

/**
 * Implementation of {@link net.pkhsolutions.idispatch.domain.resources.ResourceService}.
 */
@Service
@Transactional(propagation = Propagation.REQUIRES_NEW)
public class ResourceServiceBean implements ResourceService {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    ResourceRepository resourceRepository;

    @Autowired
    ResourceStateChangeRepository resourceStateChangeRepository;

    @Autowired
    CurrentResourceStateRepository currentResourceStateRepository;

    @Autowired
    ApplicationEventPublisher eventPublisher;

    @Override
    public ResourceStateChange getCurrentState(Resource resource) {
        final ResourceStateChange stateChange = currentResourceStateRepository.findCurrentStateByResource(checkNotNull(resource));
        if (stateChange != null) {
            return stateChange;
        } else {
            logger.debug("No state information stored for resource {}", resource);
            return new ResourceStateChange.Builder().withResource(resource).build();
        }
    }

    @Override
    public List<ResourceStateChange> getCurrentStatesOfActiveResources() {
        return addActiveResourcesWithoutStoredStates(currentResourceStateRepository.findCurrentStatesOfAllActiveResources());
    }

    /**
     * Without this method, resources that have no stored state information yet would be completely excluded from
     * {@link #getCurrentStatesOfActiveResources()}.
     */
    private <T extends Collection<ResourceStateChange>> T addActiveResourcesWithoutStoredStates(T resourceStateChanges) {
        final List<Resource> activeResources = getActiveResources();
        final Set<Resource> resourcesWithStates = resourceStateChanges.stream().map(ResourceStateChange::getResource).collect(Collectors.toSet());
        final List<Resource> resourcesWithoutStates = activeResources.stream().filter(resource -> !resourcesWithStates.contains(resource)).collect(Collectors.toList());
        resourceStateChanges.addAll(resourcesWithoutStates.stream().map((resource) -> new ResourceStateChange.Builder().withResource(resource).build()).collect(Collectors.toList()));
        return resourceStateChanges;
    }

    @Override
    public List<ResourceStateChange> getCurrentStatesOfResourcesAssignedToTicket(Ticket ticket) {
        return currentResourceStateRepository.findCurrentStatesOfActiveResourcesAssignedToTicket(checkNotNull(ticket));
    }

    @Override
    public List<Resource> getActiveResources() {
        return resourceRepository.findByActiveTrueOrderByCallSignAsc();
    }

    @Override
    public List<Resource> getAssignableResources() {
        return currentResourceStateRepository.findAssignableResources();
    }

    @Override
    public Optional<Resource> findByCallSign(String callSign) {
        if (isNullOrEmpty(callSign)) {
            return Optional.empty();
        }
        final Resource resource = resourceRepository.findByCallSign(callSign);
        return Optional.ofNullable(resource);
    }

    @Override
    public boolean assignResource(Resource resource, Ticket ticket, boolean force) {
        final ResourceStateChange currentState = getCurrentState(resource);
        if (currentState.getTicket() != null && !force) {
            return false;
        }
        logger.debug("Assigning resource {} to ticket {}", resource, ticket);
        save(new ResourceStateChange.Builder(currentState)
                .withTicket(checkNotNull(ticket))
                .withState(ResourceState.ASSIGNED)
                .build());
        return true;
    }

    @Override
    public void freeResource(Resource resource) {
        final ResourceStateChange currentState = getCurrentState(resource);
        if (currentState.getTicket() != null) {
            logger.debug("Freeing resource {} from ticket {} without changing the state", resource, currentState.getTicket());
            save(new ResourceStateChange.Builder(currentState).withTicket(null).build());
        }
    }

    @Override
    public void resourceEnRoute(Resource resource) {
        updateStateIfAssignedToOpenTicket(resource, ResourceState.EN_ROUTE);
    }

    @Override
    public void resourceOnScene(Resource resource) {
        updateStateIfAssignedToOpenTicket(resource, ResourceState.ON_SCENE);
    }

    @Override
    public void resourceAvailable(Resource resource) {
        updateState(resource, ResourceState.AVAILABLE, false);
    }

    @Override
    public void resourceAtStation(Resource resource) {
        updateState(resource, ResourceState.AT_STATION, true);
    }

    @Override
    public void resourceUnavailable(Resource resource) {
        updateState(resource, ResourceState.UNAVAILABLE, false);
    }

    private void updateStateIfAssignedToOpenTicket(Resource resource, ResourceState state) {
        final ResourceStateChange currentState = getCurrentState(resource);
        if (currentState.getTicket() != null && !currentState.getTicket().isClosed()) {
            logger.debug("Setting state of assigned resource {} to {}", resource, state);
            save(new ResourceStateChange.Builder(currentState)
                    .withState(state)
                    .build());
        }
    }

    private void updateState(Resource resource, ResourceState state, boolean resetTicket) {
        logger.debug("Setting state of resource {} to {}, resetTicket = {}", resource, state, resetTicket);
        final ResourceStateChange currentState = getCurrentState(resource);
        final ResourceStateChange.Builder builder = new ResourceStateChange.Builder(currentState)
                .withState(state);
        if (resetTicket) {
            builder.withTicket(null);
        }
        save(builder.build());
    }

    private void save(ResourceStateChange stateChange) {
        eventPublisher.publishEvent(new ResourceStateChangedEvent(this,
                resourceStateChangeRepository.saveAndFlush(stateChange)));
    }
}
