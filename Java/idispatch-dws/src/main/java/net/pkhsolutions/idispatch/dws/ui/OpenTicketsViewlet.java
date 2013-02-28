package net.pkhsolutions.idispatch.dws.ui;

import com.github.peholmst.i18n4vaadin.annotations.Message;
import com.github.peholmst.i18n4vaadin.annotations.Messages;
import com.github.wolfie.refresher.Refresher;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.event.ItemClickEvent;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.Table;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.Reindeer;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.annotation.PostConstruct;
import javax.inject.Inject;
import net.pkhsolutions.idispatch.dws.ui.tickets.TicketView;
import net.pkhsolutions.idispatch.ejb.tickets.OpenTicketDTO;
import net.pkhsolutions.idispatch.ejb.tickets.TicketEJB;

/**
 * @author peholmst
 */
public class OpenTicketsViewlet extends CustomComponent implements Refresher.RefreshListener {

    @Inject
    private TicketEJB ticketBean;
    private VerticalLayout layout;
    private Table ticketsTable;
    private BeanItemContainer<OpenTicketDTO> ticketsContainer;
    @Inject
    private OpenTicketsViewletBundle bundle;

    public OpenTicketsViewlet() {
        addStyleName("open-tickets-viewlet");
        layout = new VerticalLayout();
        layout.setMargin(false);
        layout.setSizeFull();
        setCompositionRoot(layout);
        setSizeFull();
    }

    @Messages({
        @Message(key = "caption", value = "Pågående uppdrag"),
        @Message(key = "ticketNo", value = "Nr"),
        @Message(key = "ticketType", value = "Typ"),
        @Message(key = "ticketOpened", value = "Påbörjat")
    })
    @PostConstruct
    protected void init() {
        ticketsContainer = new BeanItemContainer<>(OpenTicketDTO.class);
        ticketsTable = new Table();
        ticketsTable.setCaption(bundle.caption());
        ticketsTable.addStyleName(Reindeer.TABLE_BORDERLESS);
        ticketsTable.setSelectable(false);
        ticketsTable.setSortEnabled(false);
        ticketsTable.setContainerDataSource(ticketsContainer);
        ticketsTable.addGeneratedColumn("ticketOpenedFormatted", new Table.ColumnGenerator() {
            @Override
            public Object generateCell(Table source, Object itemId, Object columnId) {
                Calendar cal = (Calendar) source.getItem(itemId).getItemProperty("ticketOpened").getValue();
                return new SimpleDateFormat("HH:mm:ss").format(cal.getTime());
            }
        });
        ticketsTable.setVisibleColumns(new Object[]{"ticketId", "ticketOpenedFormatted", "ticketType"});
        ticketsTable.setColumnHeaders(new String[]{bundle.ticketNo(), bundle.ticketOpened(), bundle.ticketType()});
        ticketsTable.setSortContainerPropertyId("ticketOpened");
        ticketsTable.addItemClickListener(new ItemClickEvent.ItemClickListener() {
            @Override
            public void itemClick(ItemClickEvent event) {
                if (event.isDoubleClick()) {
                    Long ticketId = (Long) event.getItem().getItemProperty("ticketId").getValue();
                    getUI().getNavigator().navigateTo(TicketView.VIEW_ID + "/" + ticketId);
                }
            }
        });

        ticketsTable.setSizeFull();
        layout.addComponent(ticketsTable);
        refresh();
    }

    private synchronized void refresh() {
        List<OpenTicketDTO> openTickets = ticketBean.findOpenTickets();
        Set<OpenTicketDTO> ticketsToRemove = new HashSet<>(ticketsContainer.getItemIds());
        for (OpenTicketDTO ticket : openTickets) {
            if (!ticketsToRemove.remove(ticket)) {
                ticketsContainer.addBean(ticket);
            }
        }
        for (OpenTicketDTO ticketToRemove : ticketsToRemove) {
            ticketsContainer.removeItem(ticketToRemove);
        }
        ticketsTable.sort();
    }

    @Override
    public void refresh(Refresher source) {
        refresh();
    }
}
