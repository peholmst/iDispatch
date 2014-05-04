package net.pkhsolutions.idispatch.dws.ui.resources;

import com.vaadin.ui.Table;
import net.pkhsolutions.idispatch.common.ui.DateToStringConverter;
import net.pkhsolutions.idispatch.common.ui.resources.*;
import net.pkhsolutions.idispatch.domain.resources.ResourceStatus;
import org.springframework.context.ApplicationContext;

/**
 * Base class for tables showing {@link net.pkhsolutions.idispatch.domain.resources.ResourceStatus} beans.
 */
public abstract class AbstractResourceStatusTable extends Table {

    public AbstractResourceStatusTable(AbstractResourceStatusContainer container, ApplicationContext applicationContext) {
        addStyleName("resource-status-table");
        setContainerDataSource(container);
        setCellStyleGenerator((source, itemId, propertyId) -> "state-" + ((ResourceStatus) itemId).getState().toString().toLowerCase());

        setConverter(ResourceStatus.PROP_RESOURCE, applicationContext.getBean(ResourceToStringConverter.class));
        setConverter(ResourceStatus.PROP_TIMESTAMP, DateToStringConverter.dateTime());
        setConverter(ResourceStatus.PROP_STATE, applicationContext.getBean(ResourceStateToStringConverter.class));
        setConverter(AbstractResourceStatusContainer.NESTPROP_TICKET_TYPE, applicationContext.getBean(TicketTypeToStringConverter.class));
        setConverter(AbstractResourceStatusContainer.NESTPROP_TICKET_MUNICIPALITY, applicationContext.getBean(MunicipalityToStringConverter.class));
        setConverter(AbstractResourceStatusContainer.NESTPROP_RESOURCE_TYPE, applicationContext.getBean(ResourceTypeToStringConverter.class));

        // TODO Internationalize
        setColumnHeader(ResourceStatus.PROP_RESOURCE, "Resource");
        setColumnHeader(ResourceStatus.PROP_STATE, "State");
        setColumnHeader(ResourceStatus.PROP_TIMESTAMP, "Last changed");
        setColumnHeader(AbstractResourceStatusContainer.NESTPROP_RESOURCE_TYPE, "Type");
        setColumnHeader(AbstractResourceStatusContainer.NESTPROP_TICKET_ID, "Ticket No");
        setColumnHeader(AbstractResourceStatusContainer.NESTPROP_TICKET_TYPE, "Ticket Type");
        setColumnHeader(AbstractResourceStatusContainer.NESTPROP_TICKET_MUNICIPALITY, "Ticket Municipality");
        setColumnHeader(AbstractResourceStatusContainer.NESTPROP_TICKET_ADDRESS, "Ticket Address");
    }
}
