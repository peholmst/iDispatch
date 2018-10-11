package net.pkhapps.idispatch.web.ui.dispatch.provider;

import com.vaadin.data.ValueProvider;
import net.pkhapps.idispatch.domain.status.ResourceState;

/**
 * TODO Implement me!
 */
public class ResourceStateStyleProvider implements ValueProvider<ResourceState, String> {

    @Override
    public String apply(ResourceState resourceState) {
        if (resourceState == null) {
            return null;
        } else {
            return String.format("resource-state-%s", resourceState);
        }
    }
}
