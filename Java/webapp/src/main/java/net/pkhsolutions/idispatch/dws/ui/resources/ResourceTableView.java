package net.pkhsolutions.idispatch.dws.ui.resources;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.ui.Label;
import com.vaadin.ui.Table;
import com.vaadin.ui.VerticalLayout;
import net.pkhsolutions.idispatch.common.ui.DateToStringConverter;
import net.pkhsolutions.idispatch.common.ui.resources.*;
import net.pkhsolutions.idispatch.domain.resources.ResourceStatus;
import net.pkhsolutions.idispatch.dws.ui.DwsTheme;
import net.pkhsolutions.idispatch.dws.ui.DwsUI;
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.spring.UIScope;
import org.vaadin.spring.events.EventBus;
import org.vaadin.spring.events.EventBusScope;
import org.vaadin.spring.events.EventScope;
import org.vaadin.spring.navigator.VaadinView;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

/**
 * View for showing and modifying resource states in a table.
 */
@VaadinView(name = ResourceTableView.VIEW_NAME, ui = DwsUI.class)
@UIScope
public class ResourceTableView extends VerticalLayout implements View {

    public static final String VIEW_NAME = "resourceTable";

    private static final Object[] VISIBLE_COLUMNS = {
            ResourceStatus.PROP_RESOURCE,
            CurrentResourceStateContainer.NESTPROP_RESOURCE_TYPE,
            ResourceStatus.PROP_STATE,
            ResourceStatus.PROP_TIMESTAMP,
            CurrentResourceStateContainer.NESTPROP_TICKET_ID,
            CurrentResourceStateContainer.NESTPROP_TICKET_TYPE,
            CurrentResourceStateContainer.NESTPROP_TICKET_MUNICIPALITY,
            CurrentResourceStateContainer.NESTPROP_TICKET_ADDRESS
    };


    @Autowired
    ResourceStatusContainer container;
    @Autowired
    @EventBusScope(EventScope.APPLICATION)
    EventBus eventBus;
    @Autowired
    ResourceStateToStringConverter resourceStateToStringConverter;
    @Autowired
    ResourceToStringConverter resourceToStringConverter;
    @Autowired
    MunicipalityToStringConverter municipalityToStringConverter;
    @Autowired
    TicketTypeToStringConverter ticketTypeToStringConverter;
    @Autowired
    ResourceTypeToStringConverter resourceTypeToStringConverter;

    @PostConstruct
    void init() {
        // TODO Internationalize
        setSizeFull();
        setMargin(true);
        setSpacing(true);

        final Label title = new Label("Resource Overview");
        title.addStyleName(DwsTheme.LABEL_H1);
        addComponent(title);

        final Table table = new Table() {
            {
                addStyleName("resource-status-table");
                setSizeFull();
                setSelectable(true);
                setContainerDataSource(container);

                setConverter(ResourceStatus.PROP_RESOURCE, resourceToStringConverter);
                setConverter(ResourceStatus.PROP_TIMESTAMP, DateToStringConverter.dateTime());
                setConverter(ResourceStatus.PROP_STATE, resourceStateToStringConverter);
                setConverter(ResourceStatusContainer.NESTPROP_TICKET_TYPE, ticketTypeToStringConverter);
                setConverter(ResourceStatusContainer.NESTPROP_TICKET_MUNICIPALITY, municipalityToStringConverter);
                setConverter(ResourceStatusContainer.NESTPROP_RESOURCE_TYPE, resourceTypeToStringConverter);

                setVisibleColumns(VISIBLE_COLUMNS);

                setCellStyleGenerator((source, itemId, propertyId) -> "state-" + ((ResourceStatus) itemId).getState().toString().toLowerCase());

                setColumnHeader(ResourceStatus.PROP_RESOURCE, "Resource");
                setColumnHeader(CurrentResourceStateContainer.NESTPROP_RESOURCE_TYPE, "Type");
                setColumnHeader(ResourceStatus.PROP_STATE, "State");
                setColumnHeader(ResourceStatus.PROP_TIMESTAMP, "Last changed");
                setColumnHeader(CurrentResourceStateContainer.NESTPROP_TICKET_ID, "Ticket No");
                setColumnHeader(CurrentResourceStateContainer.NESTPROP_TICKET_TYPE, "Ticket Type");
                setColumnHeader(CurrentResourceStateContainer.NESTPROP_TICKET_MUNICIPALITY, "Ticket Municipality");
                setColumnHeader(CurrentResourceStateContainer.NESTPROP_TICKET_ADDRESS, "Ticket Address");

                setSortEnabled(true);
                setSortContainerPropertyId(ResourceStatus.PROP_RESOURCE);

            }
        };
        addComponent(table);
        setExpandRatio(table, 1f);

        // TODO Controls for changing state and assigning resources to tickets

        eventBus.subscribe(container);
    }

    @PreDestroy
    void destroy() {
        eventBus.unsubscribe(container);
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {
    }
}
