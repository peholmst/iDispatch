package net.pkhsolutions.idispatch.ui.dws.resources;

import com.vaadin.data.Container;
import com.vaadin.data.Item;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.Reindeer;
import net.pkhsolutions.idispatch.boundary.ResourceStatusService;
import net.pkhsolutions.idispatch.entity.AbstractResourceStateChange;
import net.pkhsolutions.idispatch.ui.dws.DwsUI;
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.spring.UIScope;
import org.vaadin.spring.events.EventBus;
import org.vaadin.spring.events.EventBusScope;
import org.vaadin.spring.events.EventScope;
import org.vaadin.spring.navigator.VaadinView;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

/**
 * View for checking and modifying the states of the resources by dragging and dropping resource cards between columns.
 */
@VaadinView(name = ResourceCardsView.VIEW_NAME, ui = DwsUI.class)
@UIScope
public class ResourceCardsView extends VerticalLayout implements View {

    public static final String VIEW_NAME = "resourceCards";

    @Autowired
    ResourceStateChangeContainer dataSource;

    @Autowired
    ResourceStatusService resourceService;

    @Autowired
    @EventBusScope(EventScope.APPLICATION)
    EventBus eventBus;

    private ResourceCardPanel available;
    private ResourceCardPanel atStation;
    private ResourceCardPanel assigned;
    private ResourceCardPanel dispatched;
    private ResourceCardPanel enRoute;
    private ResourceCardPanel onScene;
    private ResourceCardPanel unavailable;

    @PostConstruct
    void init() {
        addStyleName("resource-cards-view");
        setSizeFull();
        setMargin(true);
        setSpacing(true);

        final HorizontalLayout header = new HorizontalLayout();
        header.setWidth("100%");
        addComponent(header);

        final Label title = new Label("Resource Overview");
        title.setSizeUndefined();
        title.addStyleName(Reindeer.LABEL_H1);
        header.addComponent(title);

        final TextField filterResources = new TextField();
        filterResources.setInputPrompt("Filter resource cards");
        filterResources.setWidth("200px");
        filterResources.setImmediate(true);
        filterResources.addValueChangeListener(event -> {
            if (filterResources.getValue() == null || filterResources.getValue().isEmpty()) {
                dataSource.removeAllContainerFilters();
            } else {
                dataSource.addContainerFilter(new Container.Filter() {
                    @Override
                    public boolean passesFilter(Object itemId, Item item) throws UnsupportedOperationException {
                        final AbstractResourceStateChange resourceStateChange = (AbstractResourceStateChange) itemId;
                        return resourceStateChange.getResource().getCallSign().startsWith(filterResources.getValue());
                    }

                    @Override
                    public boolean appliesToProperty(Object propertyId) {
                        return "resource".equals(propertyId);
                    }
                });
            }
        });
        header.addComponent(filterResources);
        header.setComponentAlignment(filterResources, Alignment.MIDDLE_RIGHT);

        final VerticalSplitPanel splitPanel = new VerticalSplitPanel();
        splitPanel.setSizeFull();
        addComponent(splitPanel);
        setExpandRatio(splitPanel, 1);

        final HorizontalLayout statusPanels = new HorizontalLayout();
        statusPanels.setSizeFull();
        statusPanels.setSpacing(true);
        splitPanel.setFirstComponent(statusPanels);
                                 /*
        available = new ResourceCardPanel(ResourceState.AVAILABLE, "Available", dataSource, resource -> {
            resourceService.resourceAvailable(resource);
        });
        statusPanels.addComponent(available);

        atStation = new ResourceCardPanel(ResourceState.AT_STATION, "At station", dataSource, resource -> {
            resourceService.resourceAtStation(resource);
        });
        statusPanels.addComponent(atStation);

        assigned = new ResourceCardPanel(ResourceState.RESERVED, "Assigned", dataSource);
        statusPanels.addComponent(assigned);

        dispatched = new ResourceCardPanel(ResourceState.DISPATCHED, "Dispatched", dataSource);
        statusPanels.addComponent(dispatched);

        enRoute = new ResourceCardPanel(ResourceState.EN_ROUTE, "En route", dataSource, resource -> {
            // TODO Ask for ticket if not assigned
            resourceService.resourceEnRoute(resource);
        });
        statusPanels.addComponent(enRoute);

        onScene = new ResourceCardPanel(ResourceState.ON_SCENE, "On scene", dataSource, resource -> {
            // TODO Ask for ticket if not assigned
            resourceService.resourceAtStation(resource);
        });
        statusPanels.addComponent(onScene);

        unavailable = new ResourceCardPanel(ResourceState.OUT_OF_SERVICE, "Unavailable", dataSource, resource -> {
            resourceService.resourceUnavailable(resource);
        });
        statusPanels.addComponent(unavailable);    */

        dataSource.refresh();
        eventBus.subscribe(dataSource);
    }

    @PreDestroy
    void destroy() {
        eventBus.unsubscribe(dataSource);
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {
    }
}
