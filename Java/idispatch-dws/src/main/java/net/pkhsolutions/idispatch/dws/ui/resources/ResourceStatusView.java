package net.pkhsolutions.idispatch.dws.ui.units;

import com.vaadin.cdi.VaadinView;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.Reindeer;
import net.pkhsolutions.idispatch.entity.Resource;
import net.pkhsolutions.idispatch.entity.ResourceState;
import net.pkhsolutions.idispatch.entity.ResourceStatus;
import net.pkhsolutions.idispatch.entity.ResourceType;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

/**
 * @author peholmst
 */
@VaadinView(value = "resourceStatus")
public class ResourceStatusView extends CustomComponent implements View {

    private VerticalLayout layout;

    @Inject
    ResourceStatusContainer resourceStatusContainer;

    public ResourceStatusView() {
        addStyleName("resource-status-view");
        layout = new VerticalLayout();
        layout.setSizeFull();
        layout.setMargin(true);
        layout.setSpacing(true);
        setSizeFull();
        setCompositionRoot(layout);
    }

    @PostConstruct
    protected void init() {
        HorizontalLayout header = new HorizontalLayout();
        header.setWidth("100%");
        layout.addComponent(header);

        Label title = new Label("Resource Status Overview");
        title.setSizeUndefined();
        title.addStyleName(Reindeer.LABEL_H1);
        header.addComponent(title);

        TextField filterResources = new TextField();
        filterResources.setInputPrompt("Search for a resource");
        filterResources.setWidth("200px");
        header.addComponent(filterResources);
        header.setComponentAlignment(filterResources, Alignment.MIDDLE_RIGHT);

        HorizontalLayout statusPanels = new HorizontalLayout();
        statusPanels.setSizeFull();
        statusPanels.setSpacing(true);
        layout.addComponent(statusPanels);
        layout.setExpandRatio(statusPanels, 1);

        ResourceStatusCardPanel available = new ResourceStatusCardPanel(ResourceState.AVAILABLE, "Available on radio");
        available.setDataSource(resourceStatusContainer);
        statusPanels.addComponent(available);

        ResourceStatusCardPanel atStation = new ResourceStatusCardPanel(ResourceState.AT_STATION, "Available at station");
        atStation.setDataSource(resourceStatusContainer);
        statusPanels.addComponent(atStation);

        ResourceStatusCardPanel assigned = new ResourceStatusCardPanel(ResourceState.ASSIGNED, "Assigned");
        assigned.setDataSource(resourceStatusContainer);
        statusPanels.addComponent(assigned);

        ResourceStatusCardPanel dispatched = new ResourceStatusCardPanel(ResourceState.DISPATCHED, "Dispatched");
        dispatched.setDataSource(resourceStatusContainer);
        statusPanels.addComponent(dispatched);

        ResourceStatusCardPanel enRoute = new ResourceStatusCardPanel(ResourceState.EN_ROUTE, "En route");
        enRoute.setDataSource(resourceStatusContainer);
        statusPanels.addComponent(enRoute);

        ResourceStatusCardPanel onScene = new ResourceStatusCardPanel(ResourceState.ON_SCENE, "On scene");
        onScene.setDataSource(resourceStatusContainer);
        statusPanels.addComponent(onScene);

        ResourceStatusCardPanel unavailable = new ResourceStatusCardPanel(ResourceState.UNAVAILABLE, "Unavailable");
        unavailable.setDataSource(resourceStatusContainer);
        statusPanels.addComponent(unavailable);

        createDummyData();
    }

    private void createDummyData() {
        ResourceType pumper = new ResourceType.Builder().withName("Släckningsbil").build();
        ResourceType carrier = new ResourceType.Builder().withName("Tankbil").build();
        ResourceType manpower = new ResourceType.Builder().withName("Manskapsbil").build();
        ResourceType chief = new ResourceType.Builder().withName("Brandmästare").build();
        ResourceType rescueDiver = new ResourceType.Builder().withName("Räddningsdykare").build();
        ResourceType ambulance = new ResourceType.Builder().withName("Ambulans").build();

        Resource it3 = new Resource.Builder().withCallSign("Itä P3").withUnitType(chief).build();
        Resource it30 = new Resource.Builder().withCallSign("Itä P30").withUnitType(chief).build();
        Resource la3 = new Resource.Builder().withCallSign("Länsi P3").withUnitType(chief).build();
        Resource la30 = new Resource.Builder().withCallSign("Länsi P30").withUnitType(chief).build();

        Resource pg11 = new Resource.Builder().withCallSign("Pg11").withUnitType(pumper).build();
        Resource pg21 = new Resource.Builder().withCallSign("Pg21").withUnitType(pumper).build();
        Resource pg31 = new Resource.Builder().withCallSign("Pg31").withUnitType(pumper).build();

        Resource pg13 = new Resource.Builder().withCallSign("Pg13").withUnitType(carrier).build();
        Resource pg23 = new Resource.Builder().withCallSign("Pg23").withUnitType(carrier).build();

        Resource pg17 = new Resource.Builder().withCallSign("Pg17").withUnitType(manpower).build();
        Resource pg27 = new Resource.Builder().withCallSign("Pg27").withUnitType(manpower).build();
        Resource pg371 = new Resource.Builder().withCallSign("Pg27").withUnitType(manpower).build();

        Resource t15 = new Resource.Builder().withCallSign("T15").withUnitType(rescueDiver).build();
        Resource evs5211 = new Resource.Builder().withCallSign("EVS5211").withUnitType(ambulance).build();

        resourceStatusContainer.addBean(new ResourceStatus.Builder().withResource(it3).withState(ResourceState.ASSIGNED).build());
        resourceStatusContainer.addBean(new ResourceStatus.Builder().withResource(it30).withState(ResourceState.EN_ROUTE).build());
        resourceStatusContainer.addBean(new ResourceStatus.Builder().withResource(la3).withState(ResourceState.AVAILABLE).build());
        resourceStatusContainer.addBean(new ResourceStatus.Builder().withResource(la30).withState(ResourceState.AVAILABLE).build());

        resourceStatusContainer.addBean(new ResourceStatus.Builder().withResource(pg11).withState(ResourceState.UNAVAILABLE).build());
        resourceStatusContainer.addBean(new ResourceStatus.Builder().withResource(pg21).withState(ResourceState.ON_SCENE).build());
        resourceStatusContainer.addBean(new ResourceStatus.Builder().withResource(pg31).withState(ResourceState.EN_ROUTE).build());

        resourceStatusContainer.addBean(new ResourceStatus.Builder().withResource(pg13).withState(ResourceState.EN_ROUTE).build());
        resourceStatusContainer.addBean(new ResourceStatus.Builder().withResource(pg23).withState(ResourceState.ON_SCENE).build());

        resourceStatusContainer.addBean(new ResourceStatus.Builder().withResource(pg17).withState(ResourceState.AT_STATION).build());
        resourceStatusContainer.addBean(new ResourceStatus.Builder().withResource(pg27).withState(ResourceState.EN_ROUTE).build());
        resourceStatusContainer.addBean(new ResourceStatus.Builder().withResource(pg371).withState(ResourceState.AT_STATION).build());

        resourceStatusContainer.addBean(new ResourceStatus.Builder().withResource(t15).withState(ResourceState.ASSIGNED).build());
        resourceStatusContainer.addBean(new ResourceStatus.Builder().withResource(evs5211).withState(ResourceState.DISPATCHED).build());
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent viewChangeEvent) {
        //To change body of implemented methods use File | Settings | File Templates.
    }
}
