package net.pkhsolutions.idispatch.common.ui.resources;

import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.Table;
import net.pkhsolutions.idispatch.common.ui.DateToStringConverter;
import net.pkhsolutions.idispatch.domain.resources.ResourceStateChange;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.vaadin.spring.VaadinComponent;

import javax.annotation.PostConstruct;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Table view of a {@link net.pkhsolutions.idispatch.common.ui.resources.CurrentResourceStateContainer}. Please remember
 * to register the container using {@link #setContainerDataSource(CurrentResourceStateContainer)}.
 */
@VaadinComponent
@Scope("prototype")
public class CurrentResourceStateTable extends CustomComponent {

    // TODO Add table filtering

    @Autowired
    ResourceStateToStringConverter resourceStateToStringConverter;

    @Autowired
    ResourceToStringConverter resourceToStringConverter;

    @PostConstruct
    void init() {
        setCompositionRoot(new Table() {{
            setSizeFull();
            // Set empty container to get the property IDs etc.
            setContainerDataSource(new BeanItemContainer<>(ResourceStateChange.class));
            setSortEnabled(true);
            setConverter(ResourceStateChange.PROP_RESOURCE, resourceToStringConverter);
            setConverter(ResourceStateChange.PROP_TIMESTAMP, DateToStringConverter.dateTime());
            setConverter(ResourceStateChange.PROP_STATE, resourceStateToStringConverter);
            // TODO Generated columns
            setCellStyleGenerator((source, itemId, propertyId) -> "state-" + ((ResourceStateChange) itemId).getState().toString().toLowerCase());
            setVisibleColumns(
                    ResourceStateChange.PROP_RESOURCE,
                    ResourceStateChange.PROP_STATE,
                    ResourceStateChange.PROP_TIMESTAMP);
            // TODO Internationalize
            setColumnHeaders(
                    "Resource",
                    "State",
                    "Last changed");
            setSortContainerPropertyId(ResourceStateChange.PROP_RESOURCE);
        }});
        addStyleName("current-resource-state-table");
    }

    private Table getTable() {
        return (Table) getCompositionRoot();
    }

    /**
     * Sets the {@link net.pkhsolutions.idispatch.common.ui.resources.CurrentResourceStateContainer} to use.
     */
    public void setContainerDataSource(CurrentResourceStateContainer newDataSource) {
        getTable().setContainerDataSource(checkNotNull(newDataSource));
    }
}
