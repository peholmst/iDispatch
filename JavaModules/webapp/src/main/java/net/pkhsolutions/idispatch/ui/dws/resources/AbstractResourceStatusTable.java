package net.pkhsolutions.idispatch.ui.dws.resources;

import com.vaadin.ui.Table;
import net.pkhsolutions.idispatch.entity.ResourceStatus;
import net.pkhsolutions.idispatch.ui.common.DateToStringConverter;
import net.pkhsolutions.idispatch.ui.common.resources.*;
import org.springframework.context.ApplicationContext;

import java.util.Collection;

import static com.google.common.collect.Sets.newHashSet;
import static java.util.Collections.emptySet;

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
        setColumnHeader(AbstractResourceStatusContainer.NESTPROP_ASSIGNMENT_ID, "Assignment No");
        setColumnHeader(AbstractResourceStatusContainer.NESTPROP_ASSIGNMENT_TYPE, "Assignment Type");
        setColumnHeader(AbstractResourceStatusContainer.NESTPROP_ASSIGNMENT_MUNICIPALITY, "Assignment Municipality");
        setColumnHeader(AbstractResourceStatusContainer.NESTPROP_ASSIGNMENT_ADDRESS, "Assignment Address");
    }

    @SuppressWarnings("unchecked")
    public Collection<ResourceStatus> getCurrentSelection() {
        Object selection = getValue();
        if (selection instanceof Collection) {
            return (Collection<ResourceStatus>) selection;
        } else if (selection != null) {
            return newHashSet((ResourceStatus) selection);
        } else {
            return emptySet();
        }
    }
}
