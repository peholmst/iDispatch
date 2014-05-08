package net.pkhsolutions.idispatch.ui.dws.resources;

import com.vaadin.ui.Table;
import net.pkhsolutions.idispatch.entity.ResourceStatus;
import net.pkhsolutions.idispatch.ui.common.DateToStringConverter;
import net.pkhsolutions.idispatch.ui.common.resources.*;
import org.springframework.context.ApplicationContext;

/**
 * Base class for tables showing {@link net.pkhsolutions.idispatch.entity.ResourceStatus} beans.
 */
public abstract class AbstractResourceStatusTable extends Table {

    public AbstractResourceStatusTable(AbstractResourceStatusContainer container, ApplicationContext applicationContext) {
        addStyleName("resource-status-table");
        setContainerDataSource(container);
        setCellStyleGenerator((source, itemId, propertyId) -> "state-" + ((ResourceStatus) itemId).getState().toString().toLowerCase());

        setConverter(ResourceStatus.PROP_RESOURCE, applicationContext.getBean(ResourceToStringConverter.class));
        setConverter(ResourceStatus.PROP_TIMESTAMP, DateToStringConverter.dateTime());
        setConverter(ResourceStatus.PROP_STATE, applicationContext.getBean(ResourceStateToStringConverter.class));
        setConverter(AbstractResourceStatusContainer.NESTPROP_ASSIGNMENT_TYPE, applicationContext.getBean(AssignmentTypeToStringConverter.class));
        setConverter(AbstractResourceStatusContainer.NESTPROP_ASSIGNMENT_MUNICIPALITY, applicationContext.getBean(MunicipalityToStringConverter.class));
        setConverter(AbstractResourceStatusContainer.NESTPROP_RESOURCE_TYPE, applicationContext.getBean(ResourceTypeToStringConverter.class));

        // TODO Internationalize
        setColumnHeader(ResourceStatus.PROP_RESOURCE, "Resource");
        setColumnHeader(ResourceStatus.PROP_STATE, "State");
        setColumnHeader(ResourceStatus.PROP_TIMESTAMP, "Last changed");
        setColumnHeader(AbstractResourceStatusContainer.NESTPROP_RESOURCE_TYPE, "Type");
        setColumnHeader(AbstractResourceStatusContainer.NESTPROP_ASSIGNMENT_ID, "Ticket No");
        setColumnHeader(AbstractResourceStatusContainer.NESTPROP_ASSIGNMENT_TYPE, "Ticket Type");
        setColumnHeader(AbstractResourceStatusContainer.NESTPROP_ASSIGNMENT_MUNICIPALITY, "Ticket Municipality");
        setColumnHeader(AbstractResourceStatusContainer.NESTPROP_ASSIGNMENT_ADDRESS, "Ticket Address");
    }
}
