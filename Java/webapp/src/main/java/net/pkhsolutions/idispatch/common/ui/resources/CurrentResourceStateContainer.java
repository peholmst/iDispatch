package net.pkhsolutions.idispatch.common.ui.resources;

import com.vaadin.data.Item;
import com.vaadin.data.util.BeanItemContainer;
import net.pkhsolutions.idispatch.domain.resources.Resource;
import net.pkhsolutions.idispatch.domain.resources.ResourceService;
import net.pkhsolutions.idispatch.domain.resources.ResourceState;
import net.pkhsolutions.idispatch.domain.resources.ResourceStateChange;
import net.pkhsolutions.idispatch.domain.resources.events.ResourceStateChangedEvent;
import net.pkhsolutions.idispatch.domain.tickets.Ticket;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.vaadin.spring.VaadinComponent;
import org.vaadin.spring.events.Event;
import org.vaadin.spring.events.EventBusListener;

import java.util.*;

import static net.pkhsolutions.idispatch.common.utils.NestedPropertyUtils.buildNestedProperty;

/**
 * Container for the current states of all active resources. To make the container update itself automatically
 * when the state of a resource is changed, it needs to be subscribed to the application scoped {@link org.vaadin.spring.events.EventBus}.
 * Also remember to call {@link #refresh()} once you have everything set up, otherwise the container will never get populated.
 */
@VaadinComponent
@Scope("prototype")
public class CurrentResourceStateContainer extends BeanItemContainer<ResourceStateChange> implements EventBusListener<ResourceStateChangedEvent> {

    public static final String NESTPROP_RESOURCE_TYPE = buildNestedProperty(ResourceStateChange.PROP_RESOURCE, Resource.PROP_TYPE);
    public static final String NESTPROP_TICKET_ID = buildNestedProperty(ResourceStateChange.PROP_TICKET, Ticket.PROP_ID);
    public static final String NESTPROP_TICKET_ADDRESS = buildNestedProperty(ResourceStateChange.PROP_TICKET, Ticket.PROP_ADDRESS);
    public static final String NESTPROP_TICKET_TYPE = buildNestedProperty(ResourceStateChange.PROP_TICKET, Ticket.PROP_TYPE);
    public static final String NESTPROP_TICKET_MUNICIPALITY = buildNestedProperty(ResourceStateChange.PROP_TICKET, Ticket.PROP_MUNICIPALITY);
    @Autowired
    ResourceService resourceService;
    private Map<Resource, ResourceStateChange> resourceToStateMap = new HashMap<>();

    private boolean refreshed = false;

    public CurrentResourceStateContainer() {
        super(ResourceStateChange.class);
        addNestedContainerProperty(NESTPROP_TICKET_ID);
        addNestedContainerProperty(NESTPROP_TICKET_ADDRESS);
        addNestedContainerProperty(NESTPROP_TICKET_TYPE);
        addNestedContainerProperty(NESTPROP_TICKET_MUNICIPALITY);
        addNestedContainerProperty(NESTPROP_RESOURCE_TYPE);
    }

    /**
     * Populates the container with fresh data from the {@link net.pkhsolutions.idispatch.domain.resources.ResourceService}.
     */
    public void refresh() {
        final Set<ResourceStateChange> itemsToRemove = new HashSet<>(getAllItemIds());
        final List<ResourceStateChange> newItems = resourceService.getCurrentStatesOfActiveResources();
        newItems.stream().filter(newItem -> !itemsToRemove.remove(newItem)).forEach(this::addBean);
        itemsToRemove.forEach(this::removeItem);
        resourceToStateMap.clear();
        getAllItemIds().forEach(item -> resourceToStateMap.put(item.getResource(), item));
        refreshed = true;
    }

    @Override
    public void onEvent(Event<ResourceStateChangedEvent> event) {
        if (!refreshed) {
            return;
        }
        final ResourceStateChange newStatus = event.getPayload().getResourceStateChange();
        final ResourceStateChange previousValue = resourceToStateMap.put(newStatus.getResource(), newStatus);
        if (previousValue != null) {
            removeItem(previousValue);
        }
        addBean(newStatus);
    }

    /**
     * TODO Document me
     */
    public static class ResourceStateFilter implements Filter {

        private final Set<ResourceState> includedStates;

        public ResourceStateFilter(ResourceState... includedStates) {
            this.includedStates = new HashSet<>(Arrays.asList(includedStates));
        }

        @Override
        public boolean passesFilter(Object itemId, Item item) throws UnsupportedOperationException {
            return includedStates.contains(item.getItemProperty(ResourceStateChange.PROP_STATE).getValue());
        }

        @Override
        public boolean appliesToProperty(Object propertyId) {
            return ResourceStateChange.PROP_STATE.equals(propertyId);
        }
    }

    /**
     * TODO Document me
     */
    public static class IncludeTicketFilter implements Filter {
        private final Ticket ticket;

        public IncludeTicketFilter(Ticket ticket) {
            this.ticket = ticket;
        }

        @Override
        public boolean passesFilter(Object itemId, Item item) throws UnsupportedOperationException {
            return ticket.equals(item.getItemProperty(ResourceStateChange.PROP_TICKET).getValue());
        }

        @Override
        public boolean appliesToProperty(Object propertyId) {
            return ResourceStateChange.PROP_TICKET.equals(propertyId);
        }

    }
}
