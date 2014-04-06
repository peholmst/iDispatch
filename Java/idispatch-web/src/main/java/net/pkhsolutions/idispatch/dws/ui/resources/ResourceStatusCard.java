package net.pkhsolutions.idispatch.dws.ui.resources;

import com.github.peholmst.i18n4vaadin.I18N;
import com.github.peholmst.i18n4vaadin.annotations.Message;
import com.github.peholmst.i18n4vaadin.annotations.Messages;
import com.vaadin.data.util.BeanItem;
import com.vaadin.ui.Button;
import com.vaadin.ui.DragAndDropWrapper;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.Reindeer;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import javax.inject.Inject;
import net.pkhsolutions.idispatch.dws.ui.tickets.TicketView;
import net.pkhsolutions.idispatch.entity.CurrentResourceStatus;
import net.pkhsolutions.idispatch.entity.ResourceState;

public class ResourceStatusCard extends DragAndDropWrapper {

    @Inject
    private I18N i18n;
    @Inject
    private ResourceStatusCardBundle bundle;
    private CurrentResourceStatus resourceStatus;

    public ResourceStatusCard() {
        super(new VerticalLayout());
    }

    @Messages({
        @Message(key = "ticketNo", value = "Uppdrag")
    })
    public ResourceStatusCard init(final BeanItem<CurrentResourceStatus> status) {
        resourceStatus = status.getBean();
        addStyleName("resource-status-card");
        addStyleName("state-" + resourceStatus.getResourceState().toString().toLowerCase());

        setSizeUndefined();
        VerticalLayout layout = (VerticalLayout) getCompositionRoot();
        layout.setSizeUndefined();
        layout.setSpacing(true);
        layout.setMargin(true);

        Label callSign = new Label(resourceStatus.getResource().getCallSign());
        callSign.addStyleName("call-sign");
        callSign.setDescription(resourceStatus.getResource().getResourceType().getName(i18n.getLocale()));
        layout.addComponent(callSign);

        if (Arrays.asList(ResourceState.ASSIGNED, ResourceState.DISPATCHED, ResourceState.EN_ROUTE, ResourceState.ON_SCENE).contains(this.resourceStatus.getResourceState()) && this.resourceStatus.getTicket() != null) {
            String ticketDescription = String.format("%s: %s",
                    getTicketMunicipality(),
                    getTicketAddress());
            String ticketCaption = String.format("%s %d (%s%s)",
                    bundle.ticketNo(),
                    resourceStatus.getTicket().getId(),
                    getTicketTypeCode(),
                    getTicketUrgency());
            Button ticket = new Button(ticketCaption);
            ticket.setDescription(ticketDescription);
            ticket.addStyleName(Reindeer.BUTTON_LINK);
            layout.addComponent(ticket);
            ticket.addClickListener(new Button.ClickListener() {
                @Override
                public void buttonClick(Button.ClickEvent event) {
                    Long ticketId = resourceStatus.getTicket().getId();
                    getUI().getNavigator().navigateTo(TicketView.VIEW_ID + "/" + ticketId);
                }
            });
        }

        Label timestamp = new Label(formatShortTimestamp(resourceStatus.getStateChangeTimestamp().getTime()));
        timestamp.setDescription(formatLongTimestamp(resourceStatus.getStateChangeTimestamp().getTime()));
        timestamp.addStyleName("timestamp");
        layout.addComponent(timestamp);

        setDragStartMode(DragStartMode.WRAPPER);
        return this;
    }

    private String getTicketMunicipality() {
        if (resourceStatus.getTicket() == null || resourceStatus.getTicket().getMunicipality() == null) {
            return "";
        } else {
            return resourceStatus.getTicket().getMunicipality().getName(i18n.getLocale());
        }
    }

    private String getTicketAddress() {
        if (resourceStatus.getTicket() == null || resourceStatus.getTicket().getAddress() == null) {
            return "";
        } else {
            return resourceStatus.getTicket().getAddress();
        }
    }

    private String getTicketUrgency() {
        if (resourceStatus.getTicket() == null || resourceStatus.getTicket().getUrgency() == null) {
            return "";
        } else {
            return resourceStatus.getTicket().getUrgency().toString();
        }
    }

    private String getTicketTypeCode() {
        if (resourceStatus.getTicket() == null || resourceStatus.getTicket().getTicketType() == null) {
            return "";
        } else {
            return resourceStatus.getTicket().getTicketType().getCode();
        }
    }

    public CurrentResourceStatus getResourceStatus() {
        return resourceStatus;
    }

    private static String formatShortTimestamp(Date timestamp) {
        return new SimpleDateFormat("HH:mm:ss").format(timestamp);
    }

    private static String formatLongTimestamp(Date timestamp) {
        return new SimpleDateFormat("dd.MM.yyyy HH:mm:ss").format(timestamp);
    }
}
