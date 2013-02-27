package net.pkhsolutions.idispatch.dws.ui.resources;

import com.github.peholmst.i18n4vaadin.annotations.Message;
import com.vaadin.data.Container;
import com.vaadin.data.Container.ItemSetChangeEvent;
import com.vaadin.data.util.BeanItem;
import com.vaadin.event.Transferable;
import com.vaadin.event.dd.DragAndDropEvent;
import com.vaadin.event.dd.DropHandler;
import com.vaadin.event.dd.acceptcriteria.AcceptAll;
import com.vaadin.event.dd.acceptcriteria.AcceptCriterion;
import com.vaadin.ui.*;
import com.vaadin.ui.Notification.Type;
import net.pkhsolutions.idispatch.entity.ResourceState;
import net.pkhsolutions.idispatch.entity.AbstractResourceStatus;

import java.util.*;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;
import net.pkhsolutions.idispatch.dws.ui.tickets.TicketSelectionWindow;
import net.pkhsolutions.idispatch.dws.ui.tickets.TicketSelectionWindow.Callback;
import net.pkhsolutions.idispatch.ejb.resources.ResourceStatusChangedException;
import net.pkhsolutions.idispatch.entity.CurrentResourceStatus;
import net.pkhsolutions.idispatch.entity.Ticket;

public class ResourceStatusCardPanel extends DragAndDropWrapper implements DropHandler, Container.ItemSetChangeListener {

    private ResourceStatusContainer dataSource;
    private ResourceState state;
    private Map<AbstractResourceStatus, Component> resourcesInPanel = new HashMap<>();
    @Inject
    private ResourceStatusCardPanelBundle bundle;
    @Inject
    private Instance<TicketSelectionWindow> ticketWindowFactory;
    @Inject
    private Instance<ResourceStatusCard> cardFactory;

    public ResourceStatusCardPanel() {
        super(new Panel(new CssLayout()));
        addStyleName("resource-status-card-panel");
    }

    public ResourceStatusCardPanel init(ResourceState state, String caption) {
        this.state = state;
        setCaption(caption);
        setSizeFull();
        getCompositionRoot().setSizeFull();
        setDropHandler(this);
        return this;
    }

    private Panel getPanel() {
        return (Panel) getCompositionRoot();
    }

    private ComponentContainer getPanelContent() {
        return (ComponentContainer) getPanel().getContent();
    }

    public ResourceStatusContainer getDataSource() {
        return dataSource;
    }

    public void setDataSource(ResourceStatusContainer dataSource) {
        if (this.dataSource != null) {
            this.dataSource.removeItemSetChangeListener(this);
        }
        this.dataSource = dataSource;
        if (this.dataSource != null) {
            this.dataSource.addItemSetChangeListener(this);
        }
        updatePanel();
    }

    private void updatePanel() {
        Set<AbstractResourceStatus> resourcesToRemove = new HashSet<>(resourcesInPanel.keySet());
        Collection<CurrentResourceStatus> resources = getDataSource().getItemIds();
        for (AbstractResourceStatus resource : resources) {
            if (state.equals(resource.getResourceState())) {
                resourcesToRemove.remove(resource);

                if (!resourcesInPanel.containsKey(resource)) {
                    addToPanel(resource);
                }
            }
        }

        for (AbstractResourceStatus resourceToRemove : resourcesToRemove) {
            removeFromPanel(resourceToRemove);
        }

        // TODO Sort!
    }

    private void addToPanel(AbstractResourceStatus unit) {
        BeanItem<CurrentResourceStatus> unitItem = getDataSource().getItem(unit);
        ResourceStatusCard card = cardFactory.get().init(unitItem);
        getPanelContent().addComponent(card);
        resourcesInPanel.put(unit, card);
    }

    private void removeFromPanel(AbstractResourceStatus unit) {
        Component c = resourcesInPanel.get(unit);
        getPanelContent().removeComponent(c);
        resourcesInPanel.remove(unit);
    }

    @Message(key = "statusChangedException", value = "Statusinformationen hade ändrats av en annan användare. Översiktsvyn har nu uppdaterats.")
    @Override
    public void drop(DragAndDropEvent event) {
        if (state.equals(ResourceState.ASSIGNED) || state.equals(ResourceState.DISPATCHED)) {
            // These states cannot be manually changed
            return;
        }
        Transferable transferable = event.getTransferable();
        Component sourceComponent = transferable.getSourceComponent();
        if (sourceComponent instanceof ResourceStatusCard) {
            ResourceStatusCard resourceStatusCard = (ResourceStatusCard) sourceComponent;
            CurrentResourceStatus status = resourceStatusCard.getResourceStatus();
            if (state.equals(ResourceState.EN_ROUTE) || state.equals(ResourceState.ON_SCENE)) {
                if (status.getTicket() == null) {
                    promptForTicket(status);
                } else {
                    updateStatus(status);
                }
            } else {
                status.setTicket(null);
                updateStatus(status);
            }
        }
    }

    private void updateStatus(CurrentResourceStatus status) {
        status.setResourceState(state);
        try {
            getDataSource().updateStatus(status);
        } catch (ResourceStatusChangedException ex) {
            getDataSource().refresh();
            Notification.show(bundle.statusChangedException(), Type.HUMANIZED_MESSAGE);
        }
    }

    private void promptForTicket(final CurrentResourceStatus status) {
        TicketSelectionWindow window = ticketWindowFactory.get();
        window.setCallback(new Callback() {
            @Override
            public void ticketSelected(Ticket ticket) {
                if (ticket != null) {
                    status.setTicket(ticket);
                    updateStatus(status);
                }
            }
        });
        getUI().addWindow(window);
    }

    @Override
    public AcceptCriterion getAcceptCriterion() {
        return AcceptAll.get();
    }

    @Override
    public void containerItemSetChange(ItemSetChangeEvent event) {
        updatePanel();
    }
}
