package net.pkhsolutions.idispatch.ui.dws.resources;

import com.vaadin.ui.Button;
import com.vaadin.ui.DragAndDropWrapper;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.Reindeer;
import net.pkhsolutions.idispatch.entity.AbstractResourceStateChange;
import net.pkhsolutions.idispatch.entity.Resource;
import net.pkhsolutions.idispatch.entity.ResourceState;
import net.pkhsolutions.idispatch.ui.dws.assignments.AssignmentView;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

/**
 * Component that shows the status of a single resource. Designed to be used inside a {@link ResourceCardPanel} only.
 */
public class ResourceCard extends DragAndDropWrapper {

    private AbstractResourceStateChange resourceStateChange;

    private Label callSign;
    private Button ticket;
    private Label timestamp;

    // TODO Document me!

    /**
     * @param state
     * @param resourceStateChange
     */
    public ResourceCard(ResourceState state, AbstractResourceStateChange resourceStateChange) {
        super(new VerticalLayout());
        addStyleName("resource-card");
        addStyleName("state-" + state.toString().toLowerCase());

        setSizeUndefined();
        VerticalLayout layout = (VerticalLayout) getCompositionRoot();
        layout.setSizeUndefined();
        layout.setSpacing(true);
        layout.setMargin(true);

        callSign = new Label();
        callSign.addStyleName("call-sign");
        layout.addComponent(callSign);

        ticket = new Button();
        ticket.addStyleName(Reindeer.BUTTON_LINK);
        layout.addComponent(ticket);
        ticket.addClickListener(event -> {
            final Long ticketId = resourceStateChange.getAssignment().getId();
            AssignmentView.openAssignment(ticketId);
        });

        timestamp = new Label();
        timestamp.addStyleName("timestamp");
        layout.addComponent(timestamp);

        setDragStartMode(DragStartMode.WRAPPER);
        setResourceStateChange(resourceStateChange);
    }

    public void setResourceStateChange(AbstractResourceStateChange resourceStateChange) {
        this.resourceStateChange = resourceStateChange;
        callSign.setValue(resourceStateChange.getResource().getCallSign());
        callSign.setDescription(resourceStateChange.getResource().getType().getDescription());
        timestamp.setValue(formatShortTimestamp(resourceStateChange.getTimestamp()));
        timestamp.setDescription(formatLongTimestamp(resourceStateChange.getTimestamp()));
        ticket.setVisible(isTicketVisible());
        if (ticket.isVisible()) {
            final String ticketDescription = String.format("%s: %s",
                    getTicketMunicipality(),
                    getTicketAddress());
            ticket.setDescription(ticketDescription);

            final String ticketCaption = String.format("No %d (%s%s)",
                    resourceStateChange.getAssignment().getId(),
                    getTicketTypeCode(),
                    getTicketUrgency());
            ticket.setCaption(ticketCaption);
        }
    }

    private boolean isTicketVisible() {
        return Arrays.asList(ResourceState.RESERVED, ResourceState.DISPATCHED, ResourceState.EN_ROUTE, ResourceState.ON_SCENE)
                .contains(this.resourceStateChange.getState()) && this.resourceStateChange.getAssignment() != null;
    }

    private String getTicketMunicipality() {
        if (resourceStateChange.getAssignment() == null || resourceStateChange.getAssignment().getMunicipality() == null) {
            return "";
        } else {
            return resourceStateChange.getAssignment().getMunicipality().getName();
        }
    }

    private String getTicketAddress() {
        if (resourceStateChange.getAssignment() == null || resourceStateChange.getAssignment().getAddress() == null) {
            return "";
        } else {
            return resourceStateChange.getAssignment().getAddress();
        }
    }

    private String getTicketUrgency() {
        if (resourceStateChange.getAssignment() == null || resourceStateChange.getAssignment().getUrgency() == null) {
            return "";
        } else {
            return resourceStateChange.getAssignment().getUrgency().toString();
        }
    }

    private String getTicketTypeCode() {
        if (resourceStateChange.getAssignment() == null || resourceStateChange.getAssignment().getType() == null) {
            return "";
        } else {
            return resourceStateChange.getAssignment().getType().getCode();
        }
    }

    private static String formatShortTimestamp(Date timestamp) {
        return new SimpleDateFormat("HH:mm:ss").format(timestamp);
    }

    private static String formatLongTimestamp(Date timestamp) {
        return new SimpleDateFormat("dd.MM.yyyy HH:mm:ss").format(timestamp);
    }

    /**
     * @return
     */
    public Resource getResource() {
        return resourceStateChange.getResource();
    }
}
