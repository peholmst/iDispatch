/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.pkhsolutions.idispatch.dws.ui;

import com.vaadin.data.Item;
import com.vaadin.data.util.IndexedContainer;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.Table;
import com.vaadin.ui.VerticalLayout;

import java.util.Date;

/**
 *
 * @author peholmst
 */
@Deprecated
public class ReceiverStatusViewlet extends CustomComponent {

    // TODO Move to separate view instead
    private Table receiverStatusTable;

    public ReceiverStatusViewlet() {
        addStyleName("receiver-status-viewlet");
        VerticalLayout layout = new VerticalLayout();
        layout.setMargin(true);
        layout.setSizeFull();
        setCompositionRoot(layout);
        setSizeFull();

        receiverStatusTable = new Table();
        receiverStatusTable.setCaption("Receiver Status");
        receiverStatusTable.setSizeFull();
        layout.addComponent(receiverStatusTable);

        IndexedContainer dummyContainer = new IndexedContainer();
        dummyContainer.addContainerProperty("Receiver", String.class, "");
        dummyContainer.addContainerProperty("Type", String.class, "");
        dummyContainer.addContainerProperty("Resources", String.class, "");
        dummyContainer.addContainerProperty("Status", String.class, "");
        dummyContainer.addContainerProperty("Last check in", Date.class, null);

        addDummy(dummyContainer, "Pargas brandstation", "Runboard", "Pg11, Pg13, Pg17, Pg31, Pg371", "OK");
        addDummy(dummyContainer, "Pargas FBK GSM", "GSM Gateway", "Pg31, Pg371", "OK");
        addDummy(dummyContainer, "Lielax brandstation", "Runboard", "Pg21, Pg23, Pg27", "OK");
        addDummy(dummyContainer, "Lielax brandstation GSM", "GSM Gateway", "Pg21, Pg23, Pg27", "OK");

        receiverStatusTable.setContainerDataSource(dummyContainer);
    }

    private void addDummy(IndexedContainer container, String receiver, String type, String resources, String status) {
        Object itemId = container.addItem();
        Item item = container.getItem(itemId);
        item.getItemProperty("Receiver").setValue(receiver);
        item.getItemProperty("Type").setValue(type);
        item.getItemProperty("Resources").setValue(resources);
        item.getItemProperty("Status").setValue(status);
        item.getItemProperty("Last check in").setValue(new Date());
    }
}
