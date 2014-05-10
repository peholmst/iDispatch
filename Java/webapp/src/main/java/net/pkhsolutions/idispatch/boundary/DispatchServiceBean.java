package net.pkhsolutions.idispatch.boundary;

import net.pkhsolutions.idispatch.boundary.events.DispatchNotificationSent;
import net.pkhsolutions.idispatch.entity.*;
import net.pkhsolutions.idispatch.entity.repository.DestinationRepository;
import net.pkhsolutions.idispatch.entity.repository.DispatchNotificationRepository;
import net.pkhsolutions.idispatch.entity.repository.ReceiptRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.validation.Validator;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import static com.google.common.collect.Sets.newHashSet;

@Service
class DispatchServiceBean extends AbstractServiceBean implements DispatchService {

    @Autowired
    DispatchNotificationRepository dispatchNotificationRepository;
    @Autowired
    DestinationRepository destinationRepository;
    @Autowired
    ReceiptRepository receiptRepository;
    @Autowired
    ResourceStatusService resourceStatusService;
    @Autowired
    Validator validator;

    @Override
    public DispatchNotification dispatchSelectedResources(Assignment assignment, Collection<Resource> resources) throws ValidationFailedException {
        ValidationFailedException.throwIfNotEmpty(validator.validate(assignment, DispatchValidationGroup.class));
        final Collection<Resource> assignedResources = newHashSet(findAssignedResources(assignment));
        final Collection<Resource> resourcesToDispatch = resources.stream()
                .filter(resource -> assignedResources.contains(resource))
                .collect(Collectors.toList());
        final Collection<Destination> destinations = findDestinations(resourcesToDispatch);
        logger.debug("Dispatching resources {} to assignment {} using destinations {}", resourcesToDispatch, assignment, destinations);
        final DispatchNotification notification = getTxTemplate().execute(status ->
                        dispatchNotificationRepository.saveAndFlush(new DispatchNotification(assignment, assignedResources, destinations))
        );
        applicationContext.publishEvent(new DispatchNotificationSent(this, notification));
        resourcesToDispatch.forEach(resource -> resourceStatusService.setResourceState(resource, ResourceState.DISPATCHED));
        return notification;
    }

    @Override
    public DispatchNotification dispatchAllResources(Assignment assignment) throws ValidationFailedException {
        return dispatchSelectedResources(assignment, resourceStatusService.getResourcesAssignedToAssignment(assignment));
    }

    @Override
    public DispatchNotification dispatchAllReservedResources(Assignment assignment) throws ValidationFailedException {
        return dispatchSelectedResources(assignment, resourceStatusService.getStatusOfResourcesAssignedToAssignment(assignment)
                        .stream()
                        .filter(status -> status.getState() == ResourceState.RESERVED)
                        .map(ResourceStatus::getResource)
                        .collect(Collectors.toList())
        );
    }

    @Override
    public List<DispatchNotification> findDispatchNotificationsForAssignment(Assignment assignment) {
        logger.debug("Looking up dispatch notifications for assignment {}", assignment);
        return dispatchNotificationRepository.findByAssignment(assignment);
    }

    @Override
    public List<Receipt> findReceiptsForDispatchNotification(DispatchNotification dispatchNotification) {
        logger.debug("Looking up receipts for dispatch notification {}", dispatchNotification);
        return receiptRepository.findByNotification(dispatchNotification);
    }

    private Collection<Destination> findDestinations(Collection<Resource> resources) {
        return resources.stream()
                .flatMap(resource -> destinationRepository.findDestinationsForResource(resource).stream())
                .collect(Collectors.toSet());
    }

    private Collection<Resource> findAssignedResources(Assignment assignment) {
        return resourceStatusService.getResourcesAssignedToAssignment(assignment);
    }
}
