package net.pkhsolutions.idispatch.dws.ui.units;

import com.vaadin.data.util.BeanItem;
import com.vaadin.ui.Button;
import com.vaadin.ui.DragAndDropWrapper;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import net.pkhsolutions.idispatch.entity.ResourceStatus;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 * @author peholmst
 */
public class ResourceStatusCard extends DragAndDropWrapper {

    public ResourceStatusCard(BeanItem<ResourceStatus> resourceStatus) {
        super(new VerticalLayout());
        assert resourceStatus != null : "resourceStatus must not be null";
        addStyleName("resource-status-card");
        addStyleName("state-" + resourceStatus.getBean().getState().toString().toLowerCase());
        setSizeUndefined();
        VerticalLayout layout = (VerticalLayout) getCompositionRoot();
        layout.setSizeUndefined();
        layout.setSpacing(true);
        layout.setMargin(true);

        Label callSign = new Label(resourceStatus.getBean().getResource().getCallSign());
        callSign.addStyleName("call-sign");
        layout.addComponent(callSign);

        Label resourceType = new Label(resourceStatus.getBean().getResource().getResourceType().getName());
        resourceType.addStyleName("resource-type");
        layout.addComponent(resourceType);

        Button ticket;
        if (resourceStatus.getBean().getTicket() != null) {
            // TODO Create ticket button
            ticket = null;
        } else {
            ticket = null;
        }

        Label timestamp = new Label(formatShortTimestamp(resourceStatus.getBean().getTimestamp()));
        timestamp.setDescription(formatLongTimestamp(resourceStatus.getBean().getTimestamp()));
        timestamp.addStyleName("timestamp");
        layout.addComponent(timestamp);
    }

    private static String formatShortTimestamp(Date timestamp) {
        return new SimpleDateFormat("HH:mm:ss").format(timestamp);
    }

    private static String formatLongTimestamp(Date timestamp) {
        return new SimpleDateFormat("dd.MM.yyyy HH:mm:ss").format(timestamp);
    }
}
