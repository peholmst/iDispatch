package net.pkhsolutions.idispatch.dws.ui.data;

import net.pkhsolutions.idispatch.entity.ResourceType;

/**
 * @author Petter Holmström
 */
public class ResourceTypeItem extends BuilderItem<ResourceType, ResourceType.Builder> {

    public ResourceTypeItem() {
        super(ResourceType.class, ResourceType.Builder.class);
    }
}
