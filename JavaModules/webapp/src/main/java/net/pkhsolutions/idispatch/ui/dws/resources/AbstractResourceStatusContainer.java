package net.pkhsolutions.idispatch.ui.dws.resources;

import com.vaadin.data.util.BeanItemContainer;
import net.pkhsolutions.idispatch.entity.Assignment;
import net.pkhsolutions.idispatch.entity.Resource;
import net.pkhsolutions.idispatch.entity.ResourceStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

import static net.pkhsolutions.idispatch.utils.NestedPropertyUtils.buildNestedProperty;

/**
 * Base class for containers of {@link net.pkhsolutions.idispatch.entity.ResourceStatus} beans.
 */
public abstract class AbstractResourceStatusContainer extends BeanItemContainer<ResourceStatus> {

    public static final String NESTPROP_RESOURCE_TYPE = buildNestedProperty(ResourceStatus.PROP_RESOURCE, Resource.PROP_TYPE);
    public static final String NESTPROP_ASSIGNMENT_ID = buildNestedProperty(ResourceStatus.PROP_ASSIGNMENT, Assignment.PROP_ID);
    public static final String NESTPROP_ASSIGNMENT_ADDRESS = buildNestedProperty(ResourceStatus.PROP_ASSIGNMENT, Assignment.PROP_ADDRESS);
    public static final String NESTPROP_ASSIGNMENT_TYPE = buildNestedProperty(ResourceStatus.PROP_ASSIGNMENT, Assignment.PROP_TYPE);
    public static final String NESTPROP_ASSIGNMENT_MUNICIPALITY = buildNestedProperty(ResourceStatus.PROP_ASSIGNMENT, Assignment.PROP_MUNICIPALITY);
    protected final Logger logger = LoggerFactory.getLogger(getClass());

    public AbstractResourceStatusContainer() {
        super(ResourceStatus.class);
        addNestedContainerProperty(NESTPROP_ASSIGNMENT_ID);
        addNestedContainerProperty(NESTPROP_ASSIGNMENT_ADDRESS);
        addNestedContainerProperty(NESTPROP_ASSIGNMENT_TYPE);
        addNestedContainerProperty(NESTPROP_ASSIGNMENT_MUNICIPALITY);
        addNestedContainerProperty(NESTPROP_RESOURCE_TYPE);
    }

    protected abstract List<ResourceStatus> doRefresh();

    public void refresh() {
        // TODO This method could be optimized
        logger.debug("Refreshing container");
        removeAllItems();
        addAll(doRefresh());
        // TODO Sort
    }
}
