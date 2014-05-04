package net.pkhsolutions.idispatch.dws.ui.tickets;

import com.vaadin.data.Property;
import net.pkhsolutions.idispatch.domain.resources.ResourceService;
import net.pkhsolutions.idispatch.domain.resources.ResourceStatus;
import net.pkhsolutions.idispatch.domain.resources.events.ResourceStatusChangedEvent;
import net.pkhsolutions.idispatch.domain.tickets.Ticket;
import net.pkhsolutions.idispatch.dws.ui.resources.AbstractResourceStatusContainer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.vaadin.spring.events.EventBusListenerMethod;

import java.util.List;
import java.util.Objects;

import static java.util.Collections.emptyList;

/**
 * Container for the current status of all resources assigned to a {@link net.pkhsolutions.idispatch.domain.tickets.Ticket}. Remember to subscribe the container to the application
 * scoped {@link org.vaadin.spring.events.EventBus}.
 */
@Component
@Scope("prototype")
public class AssignedResourcesContainer extends AbstractResourceStatusContainer {

    @Autowired
    ResourceService resourceService;

    private TicketModel ticketModel;

    private Ticket ticket;

    public void setTicketModel(TicketModel ticketModel) {
        Assert.notNull(ticketModel, "TicketModel must not be null");
        Assert.isNull(this.ticketModel, "TicketModel has already been set");
        this.ticketModel = ticketModel;
        ((Property.ValueChangeNotifier) ticketModel.ticket()).addValueChangeListener(event -> {
            final Ticket newTicket = (Ticket) event.getProperty().getValue();
            if (!Objects.equals(ticket, newTicket)) {
                ticket = newTicket;
                refresh();
            }
        });
    }

    @Override
    protected List<ResourceStatus> doRefresh() {
        if (ticket == null) {
            return emptyList();
        } else {
            return resourceService.getCurrentStatusOfResourcesAssignedToTicket(ticket);
        }
    }

    @EventBusListenerMethod
    void onResourceStatusChangedEvent(ResourceStatusChangedEvent event) {
        refresh();
    }
}
