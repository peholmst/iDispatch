package net.pkhsolutions.idispatch.dws.ui.tickets;

import com.github.peholmst.i18n4vaadin.I18N;
import com.github.peholmst.i18n4vaadin.annotations.Message;
import com.github.peholmst.i18n4vaadin.annotations.Messages;
import com.vaadin.cdi.VaadinView;
import com.vaadin.data.Property;
import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.data.util.BeanItem;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.data.util.converter.Converter;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.shared.ui.combobox.FilteringMode;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.Reindeer;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Locale;

import javax.annotation.PostConstruct;
import javax.enterprise.event.Observes;
import javax.inject.Inject;
import net.pkhsolutions.idispatch.dws.ui.MenuViewlet;
import net.pkhsolutions.idispatch.ejb.masterdata.MunicipalityEJB;
import net.pkhsolutions.idispatch.ejb.masterdata.ResourceEJB;
import net.pkhsolutions.idispatch.ejb.masterdata.TicketTypeEJB;
import net.pkhsolutions.idispatch.ejb.tickets.NoSuchTicketException;
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
public class TicketView extends CustomComponent implements View {

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

    public TicketView() {
        addStyleName("ticket-view");
        layout = new VerticalLayout();
        layout.setMargin(true);
        layout.setSpacing(true);
        layout.setSizeFull();
        setSizeFull();
        setCompositionRoot(layout);
    }

    private static class CalendarConverter implements Converter<String, Calendar> {

        @Override
        public Calendar convertToModel(String value, Locale locale) throws ConversionException {
            return null;
        }

        @Override
        public String convertToPresentation(Calendar value, Locale locale) throws ConversionException {
            return value == null ? "" : new SimpleDateFormat("d.M.yyyy HH:mm:ss").format(value.getTime());
        }

        @Override
        public Class<Calendar> getModelType() {
            return Calendar.class;
        }

        @Override
        public Class<String> getPresentationType() {
            return String.class;
        }
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
        @Message(key = "resources.dispatched", value = "Alarmerad"),
        @Message(key = "resources.enRoute", value = "På väg"),
        @Message(key = "resources.onScene", value = "Framme"),
        @Message(key = "resources.availableOnRadio", value = "Ledig"),
        @Message(key = "resources.availableAtStation", value = "På stationen")
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

        TextField ticketNo = new TextField(bundle.ticketNo());
        fieldGroup.bind(ticketNo, "id");
        timestamps.addComponent(ticketNo);

        TextField ticketOpened = new TextField(bundle.ticketOpened());
        ticketOpened.setConverter(new CalendarConverter());
        fieldGroup.bind(ticketOpened, "ticketOpened");
        timestamps.addComponent(ticketOpened);

        TextField ticketClosed = new TextField(bundle.ticketClosed());
        ticketClosed.setConverter(new CalendarConverter());
        fieldGroup.bind(ticketClosed, "ticketClosed");
        timestamps.addComponent(ticketClosed);

        Button closeTicket = new Button(bundle.closeTicket());
        timestamps.addComponent(closeTicket);
        timestamps.setComponentAlignment(closeTicket, Alignment.BOTTOM_RIGHT);
        timestamps.setExpandRatio(closeTicket, 1);

        TextArea description = new TextArea(bundle.description());
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

        ComboBox urgency = new ComboBox(bundle.urgency(), new BeanItemContainer<>(TicketUrgency.class, Arrays.asList(TicketUrgency.values())));
        urgency.setImmediate(true);
        fieldGroup.bind(urgency, "urgency");
        urgency.addValueChangeListener(saveListener);
        classification.addComponent(urgency);

        ComboBox type = new ComboBox(bundle.incidentType(), new BeanItemContainer<>(TicketType.class, ticketTypeBean.findAll()));
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

        ComboBox municipality = new ComboBox(bundle.municipality(), new BeanItemContainer<>(Municipality.class, municipalityBean.findAll()));
        municipality.setImmediate(true);
        fieldGroup.bind(municipality, "municipality");
        municipality.addValueChangeListener(saveListener);
        municipality.setItemCaptionPropertyId("bothNames");
        municipality.setFilteringMode(FilteringMode.CONTAINS);

        location1.addComponent(municipality);

        TextField address = new TextField(bundle.address());
        address.setNullRepresentation("");
        address.setImmediate(true);
        fieldGroup.bind(address, "address");
        address.addValueChangeListener(saveListener);
        address.setWidth("100%");
        location1.addComponent(address);
        location1.setExpandRatio(address, 1);

        resourcesContainer = new BeanItemContainer<>(TicketResourceDTO.class);

        resources = new Table(bundle.resources());
        resources.setContainerDataSource(resourcesContainer);
        resources.setSizeFull();
        resources.setSortEnabled(false);
        resources.setSortContainerPropertyId("resourceCallSign");
        resources.setVisibleColumns(new Object[]{
            "resourceCallSign",
            "dispatched",
            "enRoute",
            "onScene",
            "availableOnRadio",
            "availableAtStation"});
        resources.setColumnHeaders(new String[]{
            bundle.resources_callSign(),
            bundle.resources_dispatched(),
            bundle.resources_enRoute(),
            bundle.resources_onScene(),
            bundle.resources_availableOnRadio(),
            bundle.resources_availableAtStation()
        });
        layout.addComponent(resources);
        layout.setExpandRatio(resources, 1);

        HorizontalLayout buttons = new HorizontalLayout();
        buttons.setSpacing(true);
        buttons.setWidth("100%");
        layout.addComponent(buttons);

        final ComboBox resource = new ComboBox(null, new BeanItemContainer<>(Resource.class, resourceBean.findAll()));
        resource.setItemCaptionPropertyId("callSign");
        buttons.addComponent(resource);

        Button assign = new Button("Assign", new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                Resource selectedResource = (Resource) resource.getValue();
                if (selectedResource != null) {
                    assignResource(selectedResource);
                    resource.setValue(null);
                }
                resource.focus();
            }
        });
        buttons.addComponent(assign);

        Button detach = new Button("Detach");
        buttons.addComponent(detach);
        buttons.setExpandRatio(detach, 1);

        Button dispatchAll = new Button("Dispatch all");
        dispatchAll.addStyleName("dispatch-all");
        buttons.addComponent(dispatchAll);
        buttons.setComponentAlignment(dispatchAll, Alignment.MIDDLE_RIGHT);

        Button dispatchSelected = new Button("Dispatch selected");
        dispatchSelected.addStyleName("dispatch-selected");
        buttons.addComponent(dispatchSelected);
        buttons.setComponentAlignment(dispatchSelected, Alignment.MIDDLE_RIGHT);

        Button dispatchAssigned = new Button("Dispatch assigned");
        dispatchAssigned.addStyleName("dispatch-assigned");
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
        } finally {
            changingTicket = false;
        }
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
        }
    }

    private void assignResource(Resource resource) {
        try {
            resourcesContainer.addBean(ticketBean.assignResourceToTicket(resource, getTicket()));
        } catch (NoSuchTicketException ex) {
            Notification.show(bundle.noSuchTicketException(), Notification.Type.WARNING_MESSAGE);
        } catch (TicketClosedException ex) {
            Notification.show(bundle.ticketClosedException());
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
            Ticket ticket = ((BeanItem<Ticket>) fieldGroup.getItemDataSource()).getBean();
            setTicket(ticketBean.saveTicket(ticket));
            Notification.show(bundle.ticketSaved(), Notification.Type.TRAY_NOTIFICATION);
        } catch (TicketModifiedException ex) {
            refresh();
            Notification.show(bundle.ticketModifiedException());
        } catch (TicketClosedException ex) {
            Notification.show(bundle.ticketClosedException());
        } catch (NoSuchTicketException ex) {
            Notification.show(bundle.noSuchTicketException(), Notification.Type.WARNING_MESSAGE);
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
