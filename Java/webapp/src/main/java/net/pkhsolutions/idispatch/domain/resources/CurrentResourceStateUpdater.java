package net.pkhsolutions.idispatch.domain.resources;

import net.pkhsolutions.idispatch.domain.resources.events.ResourceStateChangedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * Application event listener that makes sure the {@link net.pkhsolutions.idispatch.domain.resources.CurrentResourceState} entity
 * is updated when the state of a resource changes.
 */
@Component
class CurrentResourceStateUpdater implements ApplicationListener<ResourceStateChangedEvent> {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    CurrentResourceStateRepository repository;

    @Override
    @Transactional(propagation = Propagation.MANDATORY)
    public void onApplicationEvent(ResourceStateChangedEvent resourceStateChangedEvent) {
        final ResourceStateChange resourceStateChange = resourceStateChangedEvent.getResourceStateChange();
        final Resource resource = resourceStateChange.getResource();
        logger.debug("Updating current resource state record of resource {} to {}", resource, resourceStateChange);
        CurrentResourceState currentResourceState = repository.findOne(resource.getId());
        if (currentResourceState == null) {
            logger.debug("No current resource state record found for resource {}, creating a new one", resource);
            currentResourceState = new CurrentResourceState(resource);
        }
        currentResourceState.setLastResourceStateChange(resourceStateChange);
        repository.saveAndFlush(currentResourceState);
    }
}
