package net.pkhsolutions.idispatch.dws.ui.resources;

import com.vaadin.data.util.BeanItemContainer;
import net.pkhsolutions.idispatch.domain.resources.Resource;
import net.pkhsolutions.idispatch.domain.resources.ResourceStatus;
import net.pkhsolutions.idispatch.domain.tickets.Ticket;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import static com.google.common.collect.Sets.newHashSet;
import static net.pkhsolutions.idispatch.common.utils.NestedPropertyUtils.buildNestedProperty;

/**
 * Base class for containers of {@link net.pkhsolutions.idispatch.domain.resources.ResourceStatus} beans.
 */
public abstract class AbstractResourceStatusContainer extends BeanItemContainer<ResourceStatus> {

    public static final String NESTPROP_RESOURCE_TYPE = buildNestedProperty(ResourceStatus.PROP_RESOURCE, Resource.PROP_TYPE);
    public static final String NESTPROP_TICKET_ID = buildNestedProperty(ResourceStatus.PROP_TICKET, Ticket.PROP_ID);
    public static final String NESTPROP_TICKET_ADDRESS = buildNestedProperty(ResourceStatus.PROP_TICKET, Ticket.PROP_ADDRESS);
    public static final String NESTPROP_TICKET_TYPE = buildNestedProperty(ResourceStatus.PROP_TICKET, Ticket.PROP_TYPE);
    public static final String NESTPROP_TICKET_MUNICIPALITY = buildNestedProperty(ResourceStatus.PROP_TICKET, Ticket.PROP_MUNICIPALITY);


    public AbstractResourceStatusContainer() {
        super(ResourceStatus.class);
        addNestedContainerProperty(NESTPROP_TICKET_ID);
        addNestedContainerProperty(NESTPROP_TICKET_ADDRESS);
        addNestedContainerProperty(NESTPROP_TICKET_TYPE);
        addNestedContainerProperty(NESTPROP_TICKET_MUNICIPALITY);
        addNestedContainerProperty(NESTPROP_RESOURCE_TYPE);
    }

    protected abstract List<ResourceStatus> doRefresh();

    @PostConstruct
    public void refresh() {
        final Set<ResourceStatus> itemsToRemove = newHashSet(getAllItemIds());
        final List<ResourceStatus> newItems = doRefresh();
        newItems.forEach(newItem -> {
            if (itemsToRemove.remove(newItem)) {
                updateItem(newItem);
            } else {
                addBean(newItem);
            }
        });
        itemsToRemove.forEach(this::removeItem);
    }

    protected void updateItem(ResourceStatus updatedItem) {
        final ResourceStatus oldItem = getItem(updatedItem).getBean();
        if (!Objects.equals(oldItem.getVersion(), updatedItem.getVersion())) {
            removeItem(oldItem);
            addBean(updatedItem);
        }
    }
}
