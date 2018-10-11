package net.pkhapps.idispatch.web.ui.dispatch.model;

import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.UIScope;
import net.pkhapps.idispatch.application.overview.ResourceOverviewDTO;
import net.pkhapps.idispatch.application.overview.ResourceOverviewService;
import net.pkhapps.idispatch.domain.resource.ResourceId;
import net.pkhapps.idispatch.web.ui.common.AbstractModel;
import org.springframework.lang.NonNull;

import java.io.Serializable;
import java.time.Clock;
import java.time.Instant;
import java.util.*;

/**
 * TODO Document me!
 */
@SpringComponent
@UIScope
public class ResourceOverviewModel extends AbstractModel<ResourceOverviewModel.Observer> {

    private final ResourceOverviewService resourceOverviewService;
    private final Clock clock;

    private final Map<ResourceId, ResourceOverviewDTO> resourceOverviewMap = new HashMap<>();
    private Instant lastRefresh;

    ResourceOverviewModel(ResourceOverviewService resourceOverviewService, Clock clock) {
        this.resourceOverviewService = resourceOverviewService;
        this.clock = clock;
        refresh();
    }

    /**
     *
     */
    public void refresh() {
        if (lastRefresh == null) {
            resourceOverviewMap.clear();
            resourceOverviewService.getAll().forEach(dto -> resourceOverviewMap.put(dto.getResourceId(), dto));
            notifyObservers(observer -> observer.onAllResourcesChanged(this));
        } else {
            resourceOverviewService.getChangesSince(lastRefresh).forEach(dto -> {
                resourceOverviewMap.put(dto.getResourceId(), dto);
                notifyObservers(observer -> observer.onSingleResourceChanged(this, dto));
            });
        }
        lastRefresh = clock.instant();
    }

    /**
     * @return
     */
    @NonNull
    public Set<ResourceId> getResources() {
        return new HashSet<>(resourceOverviewMap.keySet());
    }

    /**
     * @param resourceId
     * @return
     */
    @NonNull
    public Optional<ResourceOverviewDTO> getResourceOverview(@NonNull ResourceId resourceId) {
        return Optional.ofNullable(resourceOverviewMap.get(resourceId));
    }

    /**
     *
     */
    public interface Observer extends Serializable {
        default void onAllResourcesChanged(@NonNull ResourceOverviewModel model) {
            // NOP
        }

        default void onSingleResourceChanged(@NonNull ResourceOverviewModel model,
                                             @NonNull ResourceOverviewDTO changedResource) {
            // NOP
        }
    }
}
