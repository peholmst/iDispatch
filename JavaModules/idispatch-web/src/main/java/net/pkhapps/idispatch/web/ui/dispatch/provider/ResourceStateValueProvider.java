package net.pkhapps.idispatch.web.ui.dispatch.provider;

import com.vaadin.data.ValueProvider;
import net.pkhapps.idispatch.domain.status.ResourceState;
import net.pkhapps.idispatch.web.ui.common.i18n.I18N;

/**
 * TODO implement me
 */
public class ResourceStateValueProvider implements ValueProvider<ResourceState, String> {

    private final I18N i18n;

    public ResourceStateValueProvider(I18N i18n) {
        this.i18n = i18n;
    }

    @Override
    public String apply(ResourceState resourceState) {
        if (resourceState == null) {
            return "";
        } else {
            return i18n.get(String.format("resourceState.%s", resourceState));
        }
    }
}
