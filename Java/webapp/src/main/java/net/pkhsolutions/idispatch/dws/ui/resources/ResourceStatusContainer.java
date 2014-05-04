package net.pkhsolutions.idispatch.dws.ui.resources;

import com.vaadin.data.util.BeanItemContainer;
import net.pkhsolutions.idispatch.domain.resources.Resource;
import net.pkhsolutions.idispatch.domain.resources.ResourceService;
import net.pkhsolutions.idispatch.domain.resources.ResourceStatus;
import net.pkhsolutions.idispatch.domain.resources.events.ResourceStatusChangedEvent;
import net.pkhsolutions.idispatch.domain.tickets.Ticket;
import net.pkhsolutions.idispatch.domain.tickets.events.TicketEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.vaadin.spring.events.EventBusListenerMethod;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import static com.google.common.collect.Sets.newHashSet;
import static net.pkhsolutions.idispatch.common.utils.NestedPropertyUtils.buildNestedProperty;

/**
 * Container for the current status of all active resources. Remember to subscribe the container to the application
 * scoped {@link org.vaadin.spring.events.EventBus}.
 */
@Component
@Scope("prototype")
public class ResourceStatusContainer extends BeanItemContainer<ResourceStatus> {

    public static final String NESTPROP_RESOURCE_TYPE = buildNestedProperty(ResourceStatus.PROP_RESOURCE, Resource.PROP_TYPE);
    public static final String NESTPROP_TICKET_ID = buildNestedProperty(ResourceStatus.PROP_TICKET, Ticket.PROP_ID);
    public static final String NESTPROP_TICKET_ADDRESS = buildNestedProperty(ResourceStatus.PROP_TICKET, Ticket.PROP_ADDRESS);
    public static final String NESTPROP_TICKET_TYPE = buildNestedProperty(ResourceStatus.PROP_TICKET, Ticket.PROP_TYPE);
    public static final String NESTPROP_TICKET_MUNICIPALITY = buildNestedProperty(ResourceStatus.PROP_TICKET, Ticket.PROP_MUNICIPALITY);

    @Autowired
    ResourceService resourceService;

    public ResourceStatusContainer() {
        super(ResourceStatus.class);
        addNestedContainerProperty(NESTPROP_TICKET_ID);
        addNestedContainerProperty(NESTPROP_TICKET_ADDRESS);
        addNestedContainerProperty(NESTPROP_TICKET_TYPE);
        addNestedContainerProperty(NESTPROP_TICKET_MUNICIPALITY);
        addNestedContainerProperty(NESTPROP_RESOURCE_TYPE);
    }

    @PostConstruct
    public void refresh() {
        final Set<ResourceStatus> itemsToRemove = newHashSet(getAllItemIds());
        final List<ResourceStatus> newItems = resourceService.getCurrentStatusOfActiveResources();

        newItems.forEach(newItem -> {
            if (itemsToRemove.remove(newItem)) {
                updateItem(newItem);
            } else {
                addBean(newItem);
            }
        });
        itemsToRemove.forEach(this::removeItem);
    }

    private void updateItem(ResourceStatus updatedItem) {
        final ResourceStatus oldItem = getItem(updatedItem).getBean();
        if (!Objects.equals(oldItem.getVersion(), updatedItem.getVersion())) {
            removeItem(oldItem);
            addBean(updatedItem);
        }
    }

    @EventBusListenerMethod
    void onTicketEvent(TicketEvent event) {
        refresh();
    }

    @EventBusListenerMethod
    void onResourceStatusChangedEvent(ResourceStatusChangedEvent event) {
        updateItem(event.getResourceStatus());
    }
}
