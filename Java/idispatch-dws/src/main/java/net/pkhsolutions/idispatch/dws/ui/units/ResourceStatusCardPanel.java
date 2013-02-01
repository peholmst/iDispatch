package net.pkhsolutions.idispatch.dws.ui.units;

import com.vaadin.data.Container;
import com.vaadin.data.Container.ItemSetChangeEvent;
import com.vaadin.data.util.BeanItem;
import com.vaadin.event.Transferable;
import com.vaadin.event.dd.DragAndDropEvent;
import com.vaadin.event.dd.DropHandler;
import com.vaadin.event.dd.acceptcriteria.AcceptAll;
import com.vaadin.event.dd.acceptcriteria.AcceptCriterion;
import com.vaadin.ui.*;
import net.pkhsolutions.idispatch.entity.ResourceState;
import net.pkhsolutions.idispatch.entity.ResourceStatus;

import java.util.*;

/**
 *
 * @author peholmst
 */
public class ResourceStatusCardPanel extends DragAndDropWrapper implements DropHandler, Container.ItemSetChangeListener {

    private ResourceStatusContainer dataSource;
    private ResourceState state;
    private Map<ResourceStatus, Component> resourcesInPanel = new HashMap<>();


    public ResourceStatusCardPanel(ResourceState state, String caption) {
        super(new Panel(new CssLayout()));
        addStyleName("resource-status-card-panel");
        this.state = state;
        setCaption(caption);
        setSizeFull();
        getCompositionRoot().setSizeFull();
        setDropHandler(this);
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
        Set<ResourceStatus> resourcesToRemove = new HashSet<>(resourcesInPanel.keySet());
        Collection<ResourceStatus> resources = getDataSource().getItemIds();
        for (ResourceStatus resource : resources) {
            if (state.equals(resource.getState())) {
                resourcesToRemove.remove(resource);

                if (!resourcesInPanel.containsKey(resource)) {
                    addToPanel(resource);
                }
            }
        }

        for (ResourceStatus resourceToRemove : resourcesToRemove) {
            removeFromPanel(resourceToRemove);
        }

        // TODO Sort!
    }

    private void addToPanel(ResourceStatus unit) {
        BeanItem<ResourceStatus> unitItem = getDataSource().getItem(unit);
        ResourceStatusCard card = new ResourceStatusCard(unitItem);
        getPanelContent().addComponent(card);
        resourcesInPanel.put(unit, card);
    }

    private void removeFromPanel(ResourceStatus unit) {
        Component c = resourcesInPanel.get(unit);
        getPanelContent().removeComponent(c);
        resourcesInPanel.remove(unit);
    }

    @Override
    public void drop(DragAndDropEvent event) {
        Transferable transferable = event.getTransferable();
        Component sourceComponent = transferable.getSourceComponent();
        if (sourceComponent instanceof ResourceStatusCard) {
            ResourceStatusCard resourceStatusCard = (ResourceStatusCard) sourceComponent;
            // TODO Change the state
        }
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
