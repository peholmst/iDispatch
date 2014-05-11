package net.pkhsolutions.idispatch.ui.dws.resources;

import com.vaadin.data.Container;
import com.vaadin.data.Container.ItemSetChangeEvent;
import com.vaadin.event.Transferable;
import com.vaadin.event.dd.DragAndDropEvent;
import com.vaadin.event.dd.DropHandler;
import com.vaadin.event.dd.acceptcriteria.AcceptAll;
import com.vaadin.event.dd.acceptcriteria.AcceptCriterion;
import com.vaadin.ui.*;
import net.pkhsolutions.idispatch.entity.AbstractResourceStateChange;
import net.pkhsolutions.idispatch.entity.Resource;
import net.pkhsolutions.idispatch.entity.ResourceState;

/**
 *
 */
public class ResourceCardPanel extends DragAndDropWrapper implements DropHandler, Container.ItemSetChangeListener {

    // TODO Document me!
    // TODO Maybe we could get rid of the container and let every panel communicate directly with the service / listen for events?

    private ResourceStateChangeContainer dataSource;
    private final ResourceState state;
    private final ResourceStateChangedHandler resourceStateChangedHandler;
    private final Map<Resource, ResourceCard> cardsInPanel = new HashMap<>();

    /**
     *
     */
    @FunctionalInterface
    public interface ResourceStateChangedHandler {
        /**
         * @param resource
         */
        void requestStateChange(Resource resource);
    }

    /**
     * @param state
     * @param caption
     * @param resourceStateChangedHandler
     */
    public ResourceCardPanel(ResourceState state, String caption, ResourceStateChangeContainer dataSource, ResourceStateChangedHandler resourceStateChangedHandler) {
        super(new Panel(new CssLayout()));
        addStyleName("resource-card-panel");
        this.state = state;
        this.resourceStateChangedHandler = resourceStateChangedHandler;
        setCaption(caption);
        setSizeFull();
        getCompositionRoot().setSizeFull();
        setDropHandler(this);
        setDataSource(dataSource);
    }

    /**
     * @param state
     * @param caption
     * @param dataSource
     */
    public ResourceCardPanel(ResourceState state, String caption, ResourceStateChangeContainer dataSource) {
        this(state, caption, dataSource, null);
    }

    private Panel getPanel() {
        return (Panel) getCompositionRoot();
    }

    private ComponentContainer getPanelContent() {
        return (ComponentContainer) getPanel().getContent();
    }

    /**
     * @return
     */
    public ResourceStateChangeContainer getDataSource() {
        return dataSource;
    }

    /**
     * @param dataSource
     */
    public void setDataSource(ResourceStateChangeContainer dataSource) {
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
        final Set<Resource> resourcesToRemove = new HashSet<>(cardsInPanel.keySet());
        final Collection<AbstractResourceStateChange> allResourceStates = getDataSource().getItemIds();
        for (AbstractResourceStateChange resourceStateChange : allResourceStates) {
            if (state.equals(resourceStateChange.getState())) {
                resourcesToRemove.remove(resourceStateChange);
                if (!cardsInPanel.containsKey(resourceStateChange)) {
                    addToPanel(resourceStateChange);
                } else {
                    cardsInPanel.get(resourceStateChange).setResourceStateChange(resourceStateChange);
                }
            }
        }
        for (Resource resourceToRemove : resourcesToRemove) {
            removeFromPanel(resourceToRemove);
        }
        // TODO Sort!
    }

    private void addToPanel(AbstractResourceStateChange resourceStateChange) {
        final ResourceCard card = new ResourceCard(state, resourceStateChange);
        getPanelContent().addComponent(card);
        cardsInPanel.put(resourceStateChange.getResource(), card);
    }

    private void removeFromPanel(Resource resource) {
        final ResourceCard card = cardsInPanel.get(resource);
        getPanelContent().removeComponent(card);
        cardsInPanel.remove(resource);
    }

    @Override
    public void drop(DragAndDropEvent event) {
        if (resourceStateChangedHandler == null) {
            return;
        }
        final Transferable transferable = event.getTransferable();
        final Component sourceComponent = transferable.getSourceComponent();
        if (sourceComponent instanceof ResourceCard) {
            ResourceCard resourceCard = (ResourceCard) sourceComponent;
            resourceStateChangedHandler.requestStateChange(resourceCard.getResource());
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
