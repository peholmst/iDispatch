package net.pkhsolutions.idispatch.ui.dws.resources;

import com.vaadin.data.util.BeanItemContainer;
import net.pkhsolutions.idispatch.boundary.ResourceStatusService;
import net.pkhsolutions.idispatch.entity.AbstractResourceStateChange;
import net.pkhsolutions.idispatch.entity.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.vaadin.spring.events.Event;
import org.vaadin.spring.events.EventBusListener;

import java.util.*;

/**
 * Container for the current states of all active resources. To make the container update itself automatically
 * when the state of a resource is changed, it needs to be subscribed to the application scoped {@link org.vaadin.spring.events.EventBus}.
 */
@Component
@Scope("prototype")
public class ResourceStateChangeContainer extends BeanItemContainer<AbstractResourceStateChange> implements EventBusListener<AbstractResourceStateChange> {

    @Autowired
    ResourceStatusService resourceService;

    private Map<Resource, AbstractResourceStateChange> resourceToStateMap = new HashMap<>();

    public ResourceStateChangeContainer() {
        super(AbstractResourceStateChange.class);
    }

    /**
     * Populates the container with fresh data from the resource service.
     */
    public void refresh() {
        /*final Set<AbstractResourceStateChange> itemsToRemove = new HashSet<>(getAllItemIds());
        //final List<AbstractResourceStateChange> newItems = resourceService.getCurrentStatusOfActiveResources();
        newItems.stream().filter(newItem -> !itemsToRemove.remove(newItem)).forEach(this::addBean);
        itemsToRemove.forEach(this::removeItem);
        resourceToStateMap.clear();
        getAllItemIds().forEach(item -> resourceToStateMap.put(item.getResource(), item));*/
    }

    @Override
    public void onEvent(Event<AbstractResourceStateChange> event) {
        final AbstractResourceStateChange newStatus = event.getPayload();
        final AbstractResourceStateChange previousValue = resourceToStateMap.put(newStatus.getResource(), newStatus);
        if (previousValue != null) {
            removeItem(previousValue);
        }
        addBean(newStatus);
    }
}
