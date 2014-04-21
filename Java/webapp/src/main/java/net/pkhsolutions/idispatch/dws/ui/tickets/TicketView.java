package net.pkhsolutions.idispatch.dws.ui.tickets;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.ui.Label;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import net.pkhsolutions.idispatch.common.ui.TicketForm;
import net.pkhsolutions.idispatch.domain.MunicipalityRepository;
import net.pkhsolutions.idispatch.domain.tickets.Ticket;
import net.pkhsolutions.idispatch.domain.tickets.TicketModel;
import net.pkhsolutions.idispatch.domain.tickets.TicketService;
import net.pkhsolutions.idispatch.domain.tickets.TicketTypeRepository;
import net.pkhsolutions.idispatch.dws.ui.DwsUI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Sort;
import org.vaadin.spring.navigator.VaadinView;

import javax.annotation.PostConstruct;
import java.util.Optional;

/**
 * View for editing a single ticket.
 */
@VaadinView(name = TicketView.VIEW_NAME, ui = DwsUI.class)
@Scope("prototype")
public class TicketView extends VerticalLayout implements View {

    public static final String VIEW_NAME = "ticket";

    @Autowired
    TicketModel model;

    @Autowired
    TicketForm ticketForm;

    @Autowired
    MunicipalityRepository municipalityRepository;

    @Autowired
    TicketTypeRepository ticketTypeRepository;

    @Autowired
    TicketService ticketService;

    /**
     * Navigates to the ticket with the specified ID in the current UI.
     */
    public static void openTicket(Long ticketId) {
        UI.getCurrent().getNavigator().navigateTo(VIEW_NAME + "/" + ticketId);
    }

    @PostConstruct
    void init() {
        setSizeFull();
        setMargin(true);
        setSpacing(true);

        ticketForm.setWidth("600px");
        ticketForm.setMunicipalities(municipalityRepository.findAll(new Sort("name")));
        ticketForm.setTicketTypes(ticketTypeRepository.findAll(new Sort("code")));
        addComponent(ticketForm);

        // TODO Add resources
        // TODO Add buttons for closing and dispatching
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent viewChangeEvent) {
        final Long ticketId = getTicketId(viewChangeEvent.getParameters());
        final Optional<Ticket> ticket = ticketService.loadTicket(ticketId);
        if (ticket.isPresent()) {
            model.setTicket(ticket.get());
            ticketForm.setModel(Optional.of(model));
        } else {
            ticketForm.setVisible(false);
            addComponent(new Label(String.format("You attempted to open a ticket with the ID \"%s\". Unfortunately, no such ticket exists.", viewChangeEvent.getParameters())));
        }
    }

    private Long getTicketId(String viewParameters) {
        try {
            return Long.valueOf(viewParameters);
        } catch (NumberFormatException ex) {
            return null;
        }
    }
}
