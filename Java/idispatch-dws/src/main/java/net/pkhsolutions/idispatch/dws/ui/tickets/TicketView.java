package net.pkhsolutions.idispatch.dws.ui.tickets;

import com.github.peholmst.i18n4vaadin.I18N;
import com.github.peholmst.i18n4vaadin.annotations.Message;
import com.github.peholmst.i18n4vaadin.annotations.Messages;
import com.github.wolfie.refresher.Refresher;
import com.vaadin.cdi.VaadinView;
import com.vaadin.data.Property;
import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.data.util.BeanItem;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.event.FieldEvents;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.Extension;
import com.vaadin.shared.ui.combobox.FilteringMode;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.Reindeer;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.enterprise.event.Observes;
import javax.inject.Inject;
import net.pkhsolutions.idispatch.dws.ui.MenuViewlet;
import net.pkhsolutions.idispatch.dws.ui.utils.CalendarConverter;
import net.pkhsolutions.idispatch.ejb.masterdata.MunicipalityEJB;
import net.pkhsolutions.idispatch.ejb.masterdata.ResourceEJB;
import net.pkhsolutions.idispatch.ejb.masterdata.TicketTypeEJB;
import net.pkhsolutions.idispatch.ejb.tickets.NoSuchTicketException;
import net.pkhsolutions.idispatch.ejb.tickets.ResourceNotAvailableException;
import net.pkhsolutions.idispatch.ejb.tickets.TicketClosedException;
import net.pkhsolutions.idispatch.ejb.tickets.TicketEJB;
import net.pkhsolutions.idispatch.ejb.tickets.TicketModifiedException;
import net.pkhsolutions.idispatch.ejb.tickets.TicketResourceDTO;
import net.pkhsolutions.idispatch.entity.Municipality;
import net.pkhsolutions.idispatch.entity.Resource;
import net.pkhsolutions.idispatch.entity.Ticket;
import net.pkhsolutions.idispatch.entity.TicketType;
import net.pkhsolutions.idispatch.entity.TicketUrgency;
import org.apache.commons.lang.StringUtils;

@VaadinView(value = TicketView.VIEW_ID)
public class TicketView extends CustomComponent implements View, Refresher.RefreshListener {

    public static final String VIEW_ID = "ticket";
    private VerticalLayout layout;
    private FieldGroup fieldGroup;
    private Table resources;
    private BeanItemContainer<TicketResourceDTO> resourcesContainer;
    @Inject
    private TicketTypeEJB ticketTypeBean;
    @Inject
    private MunicipalityEJB municipalityBean;
    @Inject
    private ResourceEJB resourceBean;
    @Inject
    private I18N i18n;
    @Inject
    private TicketViewBundle bundle;
    @Inject
    private TicketEJB ticketBean;
    private boolean changingTicket = false;
    private Property.ValueChangeListener saveListener = new Property.ValueChangeListener() {
        @Override
        public void valueChange(Property.ValueChangeEvent event) {
            if (!changingTicket) {
                save();
            }
        }
    };
    private Button closeTicket;
    private TextArea description;
    private ComboBox urgency;
    private ComboBox type;
    private ComboBox municipality;
    private TextField address;
    private TextField ticketNo;
    private TextField ticketOpened;
    private TextField ticketClosed;
    private ComboBox resource;
    private Button dispatchAll;
    private Button dispatchSelected;
    private Button dispatchAssigned;
    private Button assign;
    private boolean formDisabled = false;

    public TicketView() {
        addStyleName("ticket-view");
        layout = new VerticalLayout();
        layout.setMargin(true);
        layout.setSpacing(true);
        layout.setSizeFull();
        setSizeFull();
        setCompositionRoot(layout);
    }

    @Override
    public void refresh(Refresher source) {
        refreshResources();
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
        if (getUI() == null) {
            return null;
        }
        for (Extension extension : getUI().getExtensions()) {
            if (extension instanceof Refresher) {
                return (Refresher) extension;
            }
        }
        return null;
    }

    @Messages({
        @Message(key = "title", value = "Information om uppdraget"),
        @Message(key = "ticketNo", value = "Uppdragsnr"),
        @Message(key = "ticketOpened", value = "Uppdraget påbörjat"),
        @Message(key = "ticketClosed", value = "Uppdraget avslutat"),
        @Message(key = "closeTicket", value = "Avsluta uppdrag"),
        @Message(key = "description", value = "Beskrivning av vad som hänt"),
        @Message(key = "urgency", value = "Prioritet"),
        @Message(key = "incidentType", value = "Uppdragstyp"),
        @Message(key = "municipality", value = "Kommun"),
        @Message(key = "address", value = "Adress"),
        @Message(key = "resources", value = "Resurser"),
        @Message(key = "resources.callSign", value = "Resurs"),
        @Message(key = "resources.assigned", value = "Reserverad"),
        @Message(key = "resources.dispatched", value = "Alarmerad"),
        @Message(key = "resources.enRoute", value = "På väg"),
        @Message(key = "resources.onScene", value = "Framme"),
        @Message(key = "resources.availableOnRadio", value = "Ledig"),
        @Message(key = "resources.availableAtStation", value = "På stationen"),
        @Message(key = "assign", value = "Reservera för uppdraget"),
        @Message(key = "dispatchAll", value = "Alarmera alla"),
        @Message(key = "dispatchSelected", value = "Alarmera valda"),
        @Message(key = "dispatchAssigned", value = "Alarmera reserverade")
    })
    @PostConstruct
    protected void init() {
        fieldGroup = new FieldGroup();
        fieldGroup.setBuffered(false);

        Label title = new Label(bundle.title());
        title.setSizeUndefined();
        title.addStyleName(Reindeer.LABEL_H1);
        layout.addComponent(title);

        HorizontalLayout timestamps = new HorizontalLayout();
        timestamps.setSpacing(true);
        timestamps.setWidth("600px");
        layout.addComponent(timestamps);

        ticketNo = new TextField(bundle.ticketNo());
        fieldGroup.bind(ticketNo, "id");
        timestamps.addComponent(ticketNo);

        ticketOpened = new TextField(bundle.ticketOpened());
        ticketOpened.setConverter(CalendarConverter.dateTime());
        ticketOpened.setNullRepresentation("");
        fieldGroup.bind(ticketOpened, "ticketOpened");
        timestamps.addComponent(ticketOpened);

        ticketClosed = new TextField(bundle.ticketClosed());
        ticketClosed.setConverter(CalendarConverter.dateTime());
        ticketClosed.setNullRepresentation("");
        fieldGroup.bind(ticketClosed, "ticketClosed");
        timestamps.addComponent(ticketClosed);

        closeTicket = new Button(bundle.closeTicket(), new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                closeTicket();
                closeTicket.setEnabled(!formDisabled);
            }
        });
        closeTicket.setDisableOnClick(true);
        timestamps.addComponent(closeTicket);
        timestamps.setComponentAlignment(closeTicket, Alignment.BOTTOM_RIGHT);
        timestamps.setExpandRatio(closeTicket, 1);

        description = new TextArea(bundle.description());
        description.setNullRepresentation("");
        description.setImmediate(true);
        fieldGroup.bind(description, "description");
        description.addValueChangeListener(saveListener);
        description.setWidth("600px");
        description.setHeight("100px");
        layout.addComponent(description);

        HorizontalLayout classification = new HorizontalLayout();
        classification.setWidth("600px");
        classification.setSpacing(true);
        layout.addComponent(classification);

        urgency = new ComboBox(bundle.urgency(), new BeanItemContainer<>(TicketUrgency.class, Arrays.asList(TicketUrgency.values())));
        urgency.setImmediate(true);
        fieldGroup.bind(urgency, "urgency");
        urgency.addValueChangeListener(saveListener);
        classification.addComponent(urgency);

        type = new ComboBox(bundle.incidentType(), new BeanItemContainer<>(TicketType.class, ticketTypeBean.findAll()));
        type.setImmediate(true);
        fieldGroup.bind(type, "ticketType");
        type.addValueChangeListener(saveListener);
        type.setItemCaptionPropertyId("formattedDescription" + StringUtils.capitalize(i18n.getLocale().getLanguage()));
        type.setWidth("100%");
        classification.addComponent(type);
        classification.setExpandRatio(type, 1);

        HorizontalLayout location1 = new HorizontalLayout();
        location1.setWidth("600px");
        location1.setSpacing(true);
        layout.addComponent(location1);

        municipality = new ComboBox(bundle.municipality(), new BeanItemContainer<>(Municipality.class, municipalityBean.findAll()));
        municipality.setImmediate(true);
        fieldGroup.bind(municipality, "municipality");
        municipality.addValueChangeListener(saveListener);
        municipality.setItemCaptionPropertyId("bothNames");
        municipality.setFilteringMode(FilteringMode.CONTAINS);

        location1.addComponent(municipality);

        address = new TextField(bundle.address());
        address.setNullRepresentation("");
        address.setImmediate(true);
        fieldGroup.bind(address, "address");
        address.addValueChangeListener(saveListener);
        address.setWidth("100%");
        location1.addComponent(address);
        location1.setExpandRatio(address, 1);

        resourcesContainer = new BeanItemContainer<>(TicketResourceDTO.class);

        // TODO Color-code rows in resources table
        resources = new Table(bundle.resources());
        resources.setContainerDataSource(resourcesContainer);
        resources.setSizeFull();
        resources.setSelectable(true);
        resources.setMultiSelect(true);
        resources.setSortEnabled(false);
        resources.setSortContainerPropertyId("resourceCallSign");
        resources.setVisibleColumns(new Object[]{
            "resourceCallSign",
            "assigned",
            "dispatched",
            "enRoute",
            "onScene",
            "availableOnRadio",
            "availableAtStation"});
        resources.setColumnHeaders(new String[]{
            bundle.resources_callSign(),
            bundle.resources_assigned(),
            bundle.resources_dispatched(),
            bundle.resources_enRoute(),
            bundle.resources_onScene(),
            bundle.resources_availableOnRadio(),
            bundle.resources_availableAtStation()
        });
        resources.setConverter("assigned", CalendarConverter.dateTime());
        resources.setConverter("dispatched", CalendarConverter.dateTime());
        resources.setConverter("enRoute", CalendarConverter.dateTime());
        resources.setConverter("onScene", CalendarConverter.dateTime());
        resources.setConverter("availableOnRadio", CalendarConverter.dateTime());
        resources.setConverter("availableAtStation", CalendarConverter.dateTime());
        resources.setColumnWidth("assigned", 130);
        resources.setColumnWidth("dispatched", 130);
        resources.setColumnWidth("enRoute", 130);
        resources.setColumnWidth("onScene", 130);
        resources.setColumnWidth("availableOnRadio", 130);
        resources.setColumnWidth("availableAtStation", 130);
        layout.addComponent(resources);
        layout.setExpandRatio(resources, 1);

        HorizontalLayout buttons = new HorizontalLayout();
        buttons.setSpacing(true);
        buttons.setWidth("100%");
        layout.addComponent(buttons);

        resource = new ComboBox(null);
        resource.addFocusListener(new FieldEvents.FocusListener() {
            @Override
            public void focus(FieldEvents.FocusEvent event) {
                resource.setContainerDataSource(new BeanItemContainer<>(Resource.class, resourceBean.findActiveAndAvailable()));
            }
        });
        resource.setItemCaptionPropertyId("callSign");
        buttons.addComponent(resource);

        assign = new Button(bundle.assign(), new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                Resource selectedResource = (Resource) resource.getValue();
                if (selectedResource != null) {
                    assignResource(selectedResource);
                    resource.setValue(null);
                }
                resource.focus();
                assign.setEnabled(!formDisabled);
            }
        });
        assign.setDisableOnClick(true);
        buttons.addComponent(assign);
        buttons.setExpandRatio(assign, 1);

        dispatchAll = new Button(bundle.dispatchAll());
        dispatchAll.addStyleName("dispatch-all");
        dispatchAll.setDisableOnClick(true);
        buttons.addComponent(dispatchAll);
        buttons.setComponentAlignment(dispatchAll, Alignment.MIDDLE_RIGHT);

        dispatchSelected = new Button(bundle.dispatchSelected());
        dispatchSelected.addStyleName("dispatch-selected");
        dispatchSelected.setDisableOnClick(true);
        buttons.addComponent(dispatchSelected);
        buttons.setComponentAlignment(dispatchSelected, Alignment.MIDDLE_RIGHT);

        dispatchAssigned = new Button(bundle.dispatchAssigned());
        dispatchAssigned.addStyleName("dispatch-assigned");
        dispatchAssigned.setDisableOnClick(true);
        buttons.addComponent(dispatchAssigned);
        buttons.setComponentAlignment(dispatchAssigned, Alignment.MIDDLE_RIGHT);
    }

    private void setTicket(Ticket ticket) {
        changingTicket = true;
        try {
            BeanItem<Ticket> dataSource = new BeanItem<>(ticket);
            dataSource.getItemProperty("id").setReadOnly(true);
            dataSource.getItemProperty("ticketOpened").setReadOnly(true);
            dataSource.getItemProperty("ticketClosed").setReadOnly(true);
            fieldGroup.setItemDataSource(dataSource);
            refreshResources();
            if (ticket.isClosed()) {
                disableForm();
            }
        } finally {
            changingTicket = false;
        }
    }

    private void disableForm() {
        Refresher refresher = getRefresher();
        if (refresher != null) {
            refresher.removeListener(this);
        }

        fieldGroup.setReadOnly(true);
        closeTicket.setEnabled(false);
        resource.setEnabled(false);
        assign.setEnabled(false);
        dispatchAll.setEnabled(false);
        dispatchAssigned.setEnabled(false);
        dispatchSelected.setEnabled(false);
        resources.setSelectable(false);
        formDisabled = true;
    }

    private Ticket getTicket() {
        return ((BeanItem<Ticket>) fieldGroup.getItemDataSource()).getBean();
    }

    private void refresh() {
        Ticket ticket = getTicket();
        try {
            setTicket(ticketBean.refreshTicket(ticket));
        } catch (NoSuchTicketException ex) {
            Notification.show(bundle.noSuchTicketException(), Notification.Type.WARNING_MESSAGE);
            disableForm();
        }
    }

    private void refreshResources() {
        if (getTicket() == null) {
            return;
        }
        try {
            List<TicketResourceDTO> dtos = ticketBean.findResourcesForTicket(getTicket());
            Set<TicketResourceDTO> dtosToRemove = new HashSet<>(resourcesContainer.getItemIds());

            for (TicketResourceDTO dto : dtos) {
                if (!dtosToRemove.remove(dto)) {
                    resourcesContainer.addBean(dto);
                }
            }

            for (TicketResourceDTO dtoToRemove : dtosToRemove) {
                resourcesContainer.removeItem(dtoToRemove);
            }
            resources.sort();
        } catch (NoSuchTicketException ex) {
            disableForm();
        }
    }

    @Message(key = "resourceNotAvailable", value = "Resursen {0} är inte tillgänglig för alarmering")
    private void assignResource(Resource resource) {
        try {
            resourcesContainer.addBean(ticketBean.assignResourceToTicket(resource, getTicket()));
            resources.sort();
        } catch (NoSuchTicketException ex) {
            Notification.show(bundle.noSuchTicketException(), Notification.Type.WARNING_MESSAGE);
            disableForm();
        } catch (TicketClosedException ex) {
            Notification.show(bundle.ticketClosedException());
            disableForm();
        } catch (ResourceNotAvailableException ex) {
            Notification.show(bundle.resourceNotAvailable(resource.getCallSign()));
        }
    }

    @Messages({
        @Message(key = "ticketModifiedException", value = "Uppdraget har ändrats av en annan användare. Informationen har nu uppdaterats."),
        @Message(key = "ticketClosedException", value = "Uppdraget har avslutats och kan inte längre ändras."),
        @Message(key = "noSuchTicketException", value = "Uppdraget kunde inte hittas i databasen."),
        @Message(key = "ticketSaved", value = "Uppdragsinformationen har sparats i databasen.")
    })
    private void save() {
        try {
            Ticket ticket = getTicket();
            setTicket(ticketBean.saveTicket(ticket));
            Notification.show(bundle.ticketSaved(), Notification.Type.TRAY_NOTIFICATION);
        } catch (TicketModifiedException ex) {
            refresh();
            Notification.show(bundle.ticketModifiedException());
        } catch (TicketClosedException ex) {
            Notification.show(bundle.ticketClosedException());
            disableForm();
        } catch (NoSuchTicketException ex) {
            Notification.show(bundle.noSuchTicketException(), Notification.Type.WARNING_MESSAGE);
            disableForm();
        }
    }

    @Message(key = "ticketClosedMessage", value = "Uppdraget har avslutats")
    private void closeTicket() {
        try {
            Ticket ticket = getTicket();
            setTicket(ticketBean.closeTicket(ticket));
            Notification.show(bundle.ticketClosedMessage(), Notification.Type.TRAY_NOTIFICATION);
        } catch (NoSuchTicketException ex) {
            Notification.show(bundle.noSuchTicketException(), Notification.Type.WARNING_MESSAGE);
            disableForm();
        } catch (TicketClosedException ex) {
            Notification.show(bundle.ticketClosedException());
            disableForm();
        } catch (TicketModifiedException ex) {
            refresh();
            Notification.show(bundle.ticketModifiedException());
        }
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent viewChangeEvent) {
        String potentialTicketId = viewChangeEvent.getParameters();
        Ticket ticket = null;
        if (potentialTicketId != null && !potentialTicketId.isEmpty()) {
            try {
                Long ticketId = Long.parseLong(potentialTicketId);
                ticket = ticketBean.findTicketById(ticketId);
            } catch (NumberFormatException e) {
                // Ignore it and carry on.
            }
        }
        if (ticket == null) {
            ticket = ticketBean.openTicket();
        }
        setTicket(ticket);
    }

    public static class MenuItemRegistrar {

        @Inject
        private MenuItemRegistrarBundle bundle;

        @Message(key = "menuCaption", value = "Nytt uppdrag")
        public void register(@Observes MenuViewlet.MenuItemRegistrationEvent event) {
            event.getMenu().addMenuItem(bundle.menuCaption(), VIEW_ID);
        }
    }
}
