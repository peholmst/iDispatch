package net.pkhsolutions.idispatch.common.ui.resources;

import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.Table;
import net.pkhsolutions.idispatch.common.ui.DateToStringConverter;
import net.pkhsolutions.idispatch.domain.resources.ResourceStateChange;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.vaadin.spring.VaadinComponent;

import javax.annotation.PostConstruct;
import java.util.Collection;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Table view of a {@link net.pkhsolutions.idispatch.common.ui.resources.CurrentResourceStateContainer}. Please remember
 * to register the container using {@link #setContainerDataSource(CurrentResourceStateContainer, Object...)}.
 */
@VaadinComponent
@Scope("prototype")
public class CurrentResourceStateTable extends CustomComponent {

    private static final Object[] DEFAULT_VISIBLE_COLUMNS = {
            ResourceStateChange.PROP_RESOURCE,
            CurrentResourceStateContainer.NESTPROP_RESOURCE_TYPE,
            ResourceStateChange.PROP_STATE,
            ResourceStateChange.PROP_TIMESTAMP,
            CurrentResourceStateContainer.NESTPROP_TICKET_ID,
            CurrentResourceStateContainer.NESTPROP_TICKET_TYPE,
            CurrentResourceStateContainer.NESTPROP_TICKET_MUNICIPALITY,
            CurrentResourceStateContainer.NESTPROP_TICKET_ADDRESS
    };
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
        setCompositionRoot(new Table() {{
            setSizeFull();
            setSortEnabled(false);
            setSelectable(true);
            setImmediate(true);
            setMultiSelect(true);
        }});
        addStyleName("current-resource-state-table");
    }

    private Table getTable() {
        return (Table) getCompositionRoot();
    }

    /**
     * Sets the {@link net.pkhsolutions.idispatch.common.ui.resources.CurrentResourceStateContainer} to use.
     */
    public void setContainerDataSource(CurrentResourceStateContainer newDataSource, Object... visibleColumns) {
        if (visibleColumns.length == 0) {
            visibleColumns = DEFAULT_VISIBLE_COLUMNS;
        }
        final Table table = getTable();
        table.setContainerDataSource(checkNotNull(newDataSource));
        table.setConverter(ResourceStateChange.PROP_RESOURCE, resourceToStringConverter);
        table.setConverter(ResourceStateChange.PROP_TIMESTAMP, DateToStringConverter.dateTime());
        table.setConverter(ResourceStateChange.PROP_STATE, resourceStateToStringConverter);
        table.setConverter(CurrentResourceStateContainer.NESTPROP_TICKET_TYPE, ticketTypeToStringConverter);
        table.setConverter(CurrentResourceStateContainer.NESTPROP_TICKET_MUNICIPALITY, municipalityToStringConverter);
        table.setConverter(CurrentResourceStateContainer.NESTPROP_RESOURCE_TYPE, resourceTypeToStringConverter);
        table.setCellStyleGenerator((source, itemId, propertyId) -> "state-" + ((ResourceStateChange) itemId).getState().toString().toLowerCase());
        table.setVisibleColumns(visibleColumns);
        // TODO Internationalize
        table.setColumnHeader(ResourceStateChange.PROP_RESOURCE, "Resource");
        table.setColumnHeader(CurrentResourceStateContainer.NESTPROP_RESOURCE_TYPE, "Type");
        table.setColumnHeader(ResourceStateChange.PROP_STATE, "State");
        table.setColumnHeader(ResourceStateChange.PROP_TIMESTAMP, "Last changed");
        table.setColumnHeader(CurrentResourceStateContainer.NESTPROP_TICKET_ID, "Ticket No");
        table.setColumnHeader(CurrentResourceStateContainer.NESTPROP_TICKET_TYPE, "Ticket Type");
        table.setColumnHeader(CurrentResourceStateContainer.NESTPROP_TICKET_MUNICIPALITY, "Ticket Municipality");
        table.setColumnHeader(CurrentResourceStateContainer.NESTPROP_TICKET_ADDRESS, "Ticket Address");
    }

    /**
     * TODO Document me!
     */
    public Collection<ResourceStateChange> getCurrentSelection() {
        return (Collection<ResourceStateChange>) getTable().getValue();
    }
}
