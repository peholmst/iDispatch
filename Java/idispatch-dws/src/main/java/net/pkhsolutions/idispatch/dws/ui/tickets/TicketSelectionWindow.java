package net.pkhsolutions.idispatch.dws.ui.tickets;

import com.github.peholmst.i18n4vaadin.annotations.Message;
import com.github.peholmst.i18n4vaadin.annotations.Messages;
import com.vaadin.data.Property;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Table;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import javax.annotation.PostConstruct;
import javax.inject.Inject;
import net.pkhsolutions.idispatch.dws.ui.utils.CalendarConverter;
import net.pkhsolutions.idispatch.ejb.tickets.OpenTicketDTO;
import net.pkhsolutions.idispatch.ejb.tickets.TicketEJB;
import net.pkhsolutions.idispatch.entity.Ticket;

public class TicketSelectionWindow extends Window {

    @Inject
    private TicketEJB ticketBean;
    private Button select;
    private Button close;
    private Table tickets;
    private BeanItemContainer<OpenTicketDTO> container;
    @Inject
    private TicketSelectionWindowBundle bundle;
    private Callback callback;

    public static interface Callback {

        void ticketSelected(Ticket ticket);
    }

    public TicketSelectionWindow() {
        setModal(true);
        setResizable(true);
        setWidth("400px");
        setHeight("400px");
        center();
    }

    @Messages({
        @Message(key = "title", value = "Välj uppdrag"),
        @Message(key = "select", value = "Välj uppdrag och stäng"),
        @Message(key = "close", value = "Stäng utan att välja uppdrag"),
        @Message(key = "ticketNo", value = "Nr"),
        @Message(key = "ticketType", value = "Typ"),
        @Message(key = "ticketOpened", value = "Påbörjat")
    })
    @PostConstruct
    protected void init() {
        setCaption(bundle.title());
        VerticalLayout layout = new VerticalLayout();
        layout.setSizeFull();
        setContent(layout);

        layout.setMargin(true);
        layout.setSpacing(true);

        container = new BeanItemContainer<>(OpenTicketDTO.class);

        tickets = new Table();
        tickets.setSelectable(true);
        tickets.setSizeFull();
        tickets.setContainerDataSource(container);
        tickets.setVisibleColumns(new Object[]{"ticketId", "ticketOpened", "ticketType"});
        tickets.setConverter("ticketOpened", CalendarConverter.dateTime());
        tickets.setColumnHeaders(new String[]{bundle.ticketNo(), bundle.ticketOpened(), bundle.ticketType()});
        tickets.setSortContainerPropertyId("ticketOpened");
        tickets.setImmediate(true);
        tickets.addValueChangeListener(new Property.ValueChangeListener() {
            @Override
            public void valueChange(Property.ValueChangeEvent event) {
                select.setEnabled(tickets.getValue() != null);
            }
        });
        layout.addComponent(tickets);
        layout.setExpandRatio(tickets, 1);

        HorizontalLayout buttons = new HorizontalLayout();
        buttons.setSizeUndefined();
        buttons.setSpacing(true);
        layout.addComponent(buttons);
        layout.setComponentAlignment(buttons, Alignment.BOTTOM_RIGHT);

        select = new Button(bundle.select(), new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                select();
            }
        });
        select.setDisableOnClick(true);
        select.setEnabled(false);
        buttons.addComponent(select);

        close = new Button(bundle.close(), new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                justClose();
            }
        });
        close.setDisableOnClick(true);
        buttons.addComponent(close);
    }

    public void setCallback(Callback callback) {
        this.callback = callback;
    }


    private void refresh() {
        container.removeAllItems();
        container.addAll(ticketBean.findOpenTickets());
        tickets.sort();
    }

    private void select() {
        OpenTicketDTO selected = (OpenTicketDTO) tickets.getValue();
        if (selected != null && callback != null) {
            Ticket ticket = ticketBean.findTicketById(selected.getTicketId());
            callback.ticketSelected(ticket);
            justClose();
        }
    }

    private void justClose() {
        getUI().removeWindow(this);
    }

    @Override
    public void attach() {
        super.attach();
        refresh();
    }
}
