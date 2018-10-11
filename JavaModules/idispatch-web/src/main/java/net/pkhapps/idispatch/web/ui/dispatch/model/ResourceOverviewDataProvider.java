package net.pkhapps.idispatch.web.ui.dispatch.model;

import com.vaadin.data.provider.ListDataProvider;
import com.vaadin.shared.Registration;
import net.pkhapps.idispatch.application.overview.ResourceOverviewDTO;
import org.springframework.lang.NonNull;

import java.util.HashSet;
import java.util.Optional;

/**
 * TODO Document me!
 */
public class ResourceOverviewDataProvider extends ListDataProvider<ResourceOverviewDTO>
        implements ResourceOverviewModel.Observer {

    private Registration modelRegistration;

    public ResourceOverviewDataProvider() {
        super(new HashSet<>());
    }
    
    public void registerWithModel(@NonNull ResourceOverviewModel model) {
        unregisterFromModel();
        modelRegistration = model.addObserver(this);
        onAllResourcesChanged(model);
    }

    public void unregisterFromModel() {
        if (modelRegistration != null) {
            modelRegistration.remove();
        }
    }

    @Override
    public Object getId(ResourceOverviewDTO item) {
        return item.getResourceId();
    }

    @Override
    public void onAllResourcesChanged(@NonNull ResourceOverviewModel model) {
        getItems().clear();
        model.getResources().stream()
                .map(model::getResourceOverview)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .forEach(getItems()::add);
        refreshAll();
    }

    @Override
    public void onSingleResourceChanged(@NonNull ResourceOverviewModel model,
                                        @NonNull ResourceOverviewDTO changedResource) {
        getItems().removeIf(dto -> dto.getResourceId().equals(changedResource.getResourceId()));
        getItems().add(changedResource);
        refreshItem(changedResource);
    }
}
