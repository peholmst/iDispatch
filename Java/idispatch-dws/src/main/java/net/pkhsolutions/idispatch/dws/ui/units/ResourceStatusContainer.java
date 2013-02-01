package net.pkhsolutions.idispatch.dws.ui.units;

import com.vaadin.data.Container;
import com.vaadin.data.util.BeanItemContainer;
import net.pkhsolutions.idispatch.entity.ResourceStatus;

/**
 *
 * @author peholmst
 */
public class ResourceStatusContainer extends BeanItemContainer<ResourceStatus> implements Container {

    public ResourceStatusContainer() {
        super(ResourceStatus.class);
    }

    // TODO Plug in backend


}
