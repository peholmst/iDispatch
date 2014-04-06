package net.pkhsolutions.idispatch.dws.ui.resources;

import com.vaadin.data.Container;
import com.vaadin.data.util.BeanItemContainer;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.inject.Inject;
import net.pkhsolutions.idispatch.ejb.resources.ResourceStatusChangedException;
import net.pkhsolutions.idispatch.ejb.resources.ResourceStatusEJB;
import net.pkhsolutions.idispatch.entity.CurrentResourceStatus;

public class ResourceStatusContainer extends BeanItemContainer<CurrentResourceStatus> implements Container {

    @Inject
    private ResourceStatusEJB resourceStatusBean;

    public ResourceStatusContainer() {
        super(CurrentResourceStatus.class);
    }

    public void refresh() {
        Set<CurrentResourceStatus> itemsToRemove = new HashSet<>(getAllItemIds());
        List<CurrentResourceStatus> newItems = resourceStatusBean.getStatusOfAllActiveResources();

        for (CurrentResourceStatus newItem : newItems) {
            if (itemsToRemove.remove(newItem)) {
                // Check if the item has changed (use the version property since they both have the same ID)
                if (!newItem.getVersion().equals(getItem(newItem).getBean().getVersion())) {
                    removeItem(newItem); // This will remove the old version
                    addBean(newItem); // This will add the new version
                }
            } else {
                addBean(newItem);
            }
        }
        for (CurrentResourceStatus itemToRemove : itemsToRemove) {
            removeItem(itemToRemove);
        }
    }

    public void updateStatus(CurrentResourceStatus newStatus) throws ResourceStatusChangedException {
        CurrentResourceStatus updatedStatus = resourceStatusBean.updateStatus(newStatus);
        removeItem(updatedStatus);
        addBean(updatedStatus);
    }
}
