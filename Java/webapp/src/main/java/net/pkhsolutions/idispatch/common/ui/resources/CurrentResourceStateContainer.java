package net.pkhsolutions.idispatch.common.ui.resources;

import com.vaadin.data.util.BeanItemContainer;
import net.pkhsolutions.idispatch.domain.resources.Resource;
import net.pkhsolutions.idispatch.domain.resources.ResourceService;
import net.pkhsolutions.idispatch.domain.resources.ResourceStateChange;
import net.pkhsolutions.idispatch.domain.resources.events.ResourceStateChangedEvent;
import net.pkhsolutions.idispatch.domain.tickets.Ticket;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.vaadin.spring.VaadinComponent;
import org.vaadin.spring.events.Event;
import org.vaadin.spring.events.EventBusListener;

import javax.annotation.PostConstruct;
import java.util.*;

/**
 * Container for the current states of all active resources. To make the container update itself automatically
 * when the state of a resource is changed, it needs to be subscribed to the application scoped {@link org.vaadin.spring.events.EventBus}.
 */
@VaadinComponent
@Scope("prototype")
public class CurrentResourceStateContainer extends BeanItemContainer<ResourceStateChange> implements EventBusListener<ResourceStateChangedEvent> {

    @Autowired
    ResourceService resourceService;

    private Map<Resource, ResourceStateChange> resourceToStateMap = new HashMap<>();

    private Ticket ticket;

    public CurrentResourceStateContainer() {
        super(ResourceStateChange.class);
    }

    /**
     * Returns the ticket that the included resources should currently be assigned to, if any.
     */
    public Optional<Ticket> getTicket() {
        return Optional.ofNullable(ticket);
    }

    /**
     * Sets the ticket that the included resources should currently be assigned to.
     */
    public void setTicket(Ticket ticket) {
        this.ticket = ticket;
        refresh();
    }

    /**
     * Populates the container with fresh data from the {@link net.pkhsolutions.idispatch.domain.resources.ResourceService}.
     */
    @PostConstruct
    public void refresh() {
        final Set<ResourceStateChange> itemsToRemove = new HashSet<>(getAllItemIds());
        List<ResourceStateChange> newItems;
        if (getTicket().isPresent()) {
            newItems = resourceService.getCurrentStatesOfResourcesAssignedToTicket(getTicket().get());
        } else {
            newItems = resourceService.getCurrentStatesOfActiveResources();
        }
        newItems.stream().filter(newItem -> !itemsToRemove.remove(newItem)).forEach(this::addBean);
        itemsToRemove.forEach(this::removeItem);
        resourceToStateMap.clear();
        getAllItemIds().forEach(item -> resourceToStateMap.put(item.getResource(), item));
    }

    @Override
    public void onEvent(Event<ResourceStateChangedEvent> event) {
        final ResourceStateChange newStatus = event.getPayload().getResourceStateChange();
        final ResourceStateChange previousValue = resourceToStateMap.put(newStatus.getResource(), newStatus);
        if (previousValue != null) {
            removeItem(previousValue);
        }
        if (!getTicket().isPresent() || getTicket().get().equals(newStatus.getTicket())) {
            addBean(newStatus);
        }
    }
}
