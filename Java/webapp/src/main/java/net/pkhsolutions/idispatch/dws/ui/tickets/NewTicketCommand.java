package net.pkhsolutions.idispatch.dws.ui.tickets;

import com.vaadin.ui.MenuBar;
import net.pkhsolutions.idispatch.domain.tickets.TicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.vaadin.spring.VaadinComponent;

/**
 * Menu bar command that creates and opens a new ticket.
 */
@VaadinComponent
@Scope("prototype")
public class NewTicketCommand implements MenuBar.Command {

    @Autowired
    TicketService ticketService;

    @Override
    public void menuSelected(MenuBar.MenuItem selectedItem) {
        final Long newTicketId = ticketService.createTicket();
        TicketView.openTicket(newTicketId);
    }
}
