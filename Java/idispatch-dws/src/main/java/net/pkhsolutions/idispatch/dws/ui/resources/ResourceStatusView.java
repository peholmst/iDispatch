package net.pkhsolutions.idispatch.dws.ui.resources;

import com.github.peholmst.i18n4vaadin.I18N;
import com.github.peholmst.i18n4vaadin.annotations.Message;
import com.github.peholmst.i18n4vaadin.annotations.Messages;
import com.github.wolfie.refresher.Refresher;
import com.vaadin.cdi.VaadinView;
import com.vaadin.cdi.component.JaasTools;
import com.vaadin.data.Container;
import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.data.util.converter.Converter;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.Extension;
import com.vaadin.server.ThemeResource;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.Reindeer;
import java.util.Locale;
import net.pkhsolutions.idispatch.entity.ResourceState;

import javax.annotation.PostConstruct;
import javax.enterprise.event.Observes;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;
import net.pkhsolutions.idispatch.dws.ui.MenuViewlet;
import net.pkhsolutions.idispatch.dws.ui.utils.CalendarConverter;
import net.pkhsolutions.idispatch.ejb.common.Roles;
import net.pkhsolutions.idispatch.entity.CurrentResourceStatus;
import net.pkhsolutions.idispatch.entity.Resource;

@VaadinView(value = ResourceStatusView.VIEW_ID, rolesAllowed = Roles.DISPATCHER)
public class ResourceStatusView extends CustomComponent implements View, Refresher.RefreshListener {

    public static final String VIEW_ID = "resourceStatus";
    private VerticalLayout layout;
    @Inject
    private ResourceStatusContainer resourceStatusContainer;
    @Inject
    private ResourceStatusViewBundle bundle;
    @Inject
    private Instance<ResourceStatusCardPanel> panelFactory;
    @Inject
    private I18N i18n;
    private Table statusTable;
    private ResourceStatusCardPanel unavailable;
    private ResourceStatusCardPanel onScene;
    private ResourceStatusCardPanel enRoute;
    private ResourceStatusCardPanel dispatched;
    private ResourceStatusCardPanel assigned;
    private ResourceStatusCardPanel atStation;
    private ResourceStatusCardPanel available;

    public ResourceStatusView() {
        addStyleName("resource-status-view");
        layout = new VerticalLayout();
        layout.setSizeFull();
        layout.setMargin(true);
        layout.setSpacing(true);
        setSizeFull();
        setCompositionRoot(layout);
    }

    @Messages({
        @Message(key = "status.available", value = "Ledig på radio"),
        @Message(key = "status.atStation", value = "Ledig på stationen"),
        @Message(key = "status.assigned", value = "Reserverad för uppdrag"),
        @Message(key = "status.dispatched", value = "Alarmerad"),
        @Message(key = "status.enRoute", value = "På väg"),
        @Message(key = "status.onScene", value = "Framme"),
        @Message(key = "status.unavailable", value = "Ej alarmerbar"),
        @Message(key = "search", value = "Sök efter resurs"),
        @Message(key = "title", value = "Resursöversikt"),
        @Message(key = "column.resource", value = "Resurs"),
        @Message(key = "column.state", value = "Status"),
        @Message(key = "column.timestamp", value = "Senast ändrad"),
        @Message(key = "column.ticketNo", value = "Uppdragsnr"),
        @Message(key = "column.ticketCode", value = "Uppdragskod"),
        @Message(key = "column.ticketMunicipality", value = "Kommun"),
        @Message(key = "column.ticketAddress", value = "Adress")
    })
    @PostConstruct
    protected void init() {
        HorizontalLayout header = new HorizontalLayout();
        header.setWidth("100%");
        layout.addComponent(header);

        Label title = new Label(bundle.title());
        title.setSizeUndefined();
        title.addStyleName(Reindeer.LABEL_H1);
        header.addComponent(title);

        final TextField filterResources = new TextField();
        filterResources.setInputPrompt(bundle.search());
        filterResources.setWidth("200px");
        filterResources.setImmediate(true);
        filterResources.addValueChangeListener(new Property.ValueChangeListener() {
            @Override
            public void valueChange(Property.ValueChangeEvent event) {
                if (filterResources.getValue() == null || filterResources.getValue().isEmpty()) {
                    resourceStatusContainer.removeAllContainerFilters();
                } else {
                    resourceStatusContainer.addContainerFilter(new Container.Filter() {
                        @Override
                        public boolean passesFilter(Object itemId, Item item) throws UnsupportedOperationException {
                            CurrentResourceStatus status = (CurrentResourceStatus) itemId;
                            return status.getResource().getCallSign().startsWith(filterResources.getValue());
                        }

                        @Override
                        public boolean appliesToProperty(Object propertyId) {
                            return "resource".equals(propertyId);
                        }
                    });
                }
            }
        });
        header.addComponent(filterResources);
        header.setComponentAlignment(filterResources, Alignment.MIDDLE_RIGHT);

        VerticalSplitPanel splitPanel = new VerticalSplitPanel();
        splitPanel.setSizeFull();
        layout.addComponent(splitPanel);
        layout.setExpandRatio(splitPanel, 1);

        HorizontalLayout statusPanels = new HorizontalLayout();
        statusPanels.setSizeFull();
        statusPanels.setSpacing(true);
        splitPanel.setFirstComponent(statusPanels);

        available = panelFactory.get().init(ResourceState.AVAILABLE, bundle.status_available());
        available.setDataSource(resourceStatusContainer);
        statusPanels.addComponent(available);

        atStation = panelFactory.get().init(ResourceState.AT_STATION, bundle.status_atStation());
        atStation.setDataSource(resourceStatusContainer);
        statusPanels.addComponent(atStation);

        assigned = panelFactory.get().init(ResourceState.ASSIGNED, bundle.status_assigned());
        assigned.setDataSource(resourceStatusContainer);
        statusPanels.addComponent(assigned);

        dispatched = panelFactory.get().init(ResourceState.DISPATCHED, bundle.status_dispatched());
        dispatched.setDataSource(resourceStatusContainer);
        statusPanels.addComponent(dispatched);

        enRoute = panelFactory.get().init(ResourceState.EN_ROUTE, bundle.status_enRoute());
        enRoute.setDataSource(resourceStatusContainer);
        statusPanels.addComponent(enRoute);

        onScene = panelFactory.get().init(ResourceState.ON_SCENE, bundle.status_onScene());
        onScene.setDataSource(resourceStatusContainer);
        statusPanels.addComponent(onScene);

        unavailable = panelFactory.get().init(ResourceState.UNAVAILABLE, bundle.status_unavailable());
        unavailable.setDataSource(resourceStatusContainer);
        statusPanels.addComponent(unavailable);

        statusTable = new Table();
        statusTable.setSizeFull();
        splitPanel.setSecondComponent(statusTable);
        statusTable.setContainerDataSource(resourceStatusContainer);
        statusTable.setSortEnabled(true);
        statusTable.setConverter("resource", new Converter<String, Resource>() {
            @Override
            public Resource convertToModel(String value, Locale locale) throws Converter.ConversionException {
                return null;
            }

            @Override
            public String convertToPresentation(Resource value, Locale locale) throws Converter.ConversionException {
                return value == null ? null : value.getCallSign();
            }

            @Override
            public Class<Resource> getModelType() {
                return Resource.class;
            }

            @Override
            public Class<String> getPresentationType() {
                return String.class;
            }
        });
        statusTable.setConverter("stateChangeTimestamp", CalendarConverter.dateTime());
        statusTable.setConverter("resourceState", new Converter<String, ResourceState>() {
            @Override
            public ResourceState convertToModel(String value, Locale locale) throws Converter.ConversionException {
                return null;
            }

            @Override
            public String convertToPresentation(ResourceState value, Locale locale) throws Converter.ConversionException {
                switch (value) {
                    case ASSIGNED:
                        return bundle.status_assigned();
                    case AT_STATION:
                        return bundle.status_atStation();
                    case AVAILABLE:
                        return bundle.status_available();
                    case DISPATCHED:
                        return bundle.status_dispatched();
                    case EN_ROUTE:
                        return bundle.status_enRoute();
                    case ON_SCENE:
                        return bundle.status_onScene();
                    case UNAVAILABLE:
                        return bundle.status_unavailable();
                    default:
                        return "";
                }
            }

            @Override
            public Class<ResourceState> getModelType() {
                return ResourceState.class;
            }

            @Override
            public Class<String> getPresentationType() {
                return String.class;
            }
        });
        statusTable.addGeneratedColumn("ticketNo", new Table.ColumnGenerator() {
            @Override
            public Object generateCell(Table source, Object itemId, Object columnId) {
                CurrentResourceStatus status = (CurrentResourceStatus) itemId;
                return status.getTicket() == null ? new Label() : new Label(status.getTicket().getId().toString());
            }
        });
        statusTable.addGeneratedColumn("ticketCode", new Table.ColumnGenerator() {
            @Override
            public Object generateCell(Table source, Object itemId, Object columnId) {
                CurrentResourceStatus status = (CurrentResourceStatus) itemId;
                return (status.getTicket() == null || status.getTicket().getTicketType() == null) ? new Label() : new Label(status.getTicket().getTicketType().getCode() + status.getTicket().getUrgency());
            }
        });
        statusTable.addGeneratedColumn("ticketMunicipality", new Table.ColumnGenerator() {
            @Override
            public Object generateCell(Table source, Object itemId, Object columnId) {
                CurrentResourceStatus status = (CurrentResourceStatus) itemId;
                return (status.getTicket() == null || status.getTicket().getMunicipality() == null) ? new Label() : new Label(status.getTicket().getMunicipality().getName(i18n.getLocale()));
            }
        });
        statusTable.addGeneratedColumn("ticketAddress", new Table.ColumnGenerator() {
            @Override
            public Object generateCell(Table source, Object itemId, Object columnId) {
                CurrentResourceStatus status = (CurrentResourceStatus) itemId;
                return status.getTicket() == null ? new Label() : new Label(status.getTicket().getAddress());
            }
        });
        statusTable.setCellStyleGenerator(new Table.CellStyleGenerator() {
            @Override
            public String getStyle(Table source, Object itemId, Object propertyId) {
                CurrentResourceStatus status = (CurrentResourceStatus) itemId;
                return "state-" + status.getResourceState().toString().toLowerCase();
            }
        });
        statusTable.setVisibleColumns(new Object[]{
            "resource",
            "resourceState",
            "stateChangeTimestamp",
            "ticketNo",
            "ticketCode",
            "ticketMunicipality",
            "ticketAddress"});
        statusTable.setColumnHeaders(new String[]{
            bundle.column_resource(),
            bundle.column_state(),
            bundle.column_timestamp(),
            bundle.column_ticketNo(),
            bundle.column_ticketCode(),
            bundle.column_ticketMunicipality(),
            bundle.column_ticketAddress()
        });
        statusTable.setSortContainerPropertyId("resource");
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent viewChangeEvent) {
        resourceStatusContainer.refresh();
        statusTable.sort();
    }

    @Override
    public void refresh(Refresher source) {
        resourceStatusContainer.refresh();
        statusTable.sort();
    }

    @Override
    public void attach() {
        super.attach();
        getRefresher().addListener(this);
    }

    @Override
    public void detach() {
        getRefresher().removeListener(this);
        super.detach();
    }

    private Refresher getRefresher() {
        for (Extension extension : getUI().getExtensions()) {
            if (extension instanceof Refresher) {
                return (Refresher) extension;
            }
        }
        return null;
    }

    public static class MenuItemRegistrar {

        @Inject
        private ResourceStatusViewBundle bundle;

        public void register(@Observes MenuViewlet.MenuItemRegistrationEvent event) {
            if (JaasTools.isUserInRole(Roles.DISPATCHER)) {
                event.getMenu().addMenuItem(bundle.title(), VIEW_ID, new ThemeResource("icons/resource_status.png"), 1);
            }
        }
    }
}
