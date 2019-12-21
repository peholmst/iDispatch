package net.pkhapps.idispatch.core.client.resourcestatus;

import net.pkhapps.idispatch.core.client.CachedDataListingService;
import org.jetbrains.annotations.NotNull;

import java.util.stream.Stream;

/**
 * Application service for listing and looking up {@link ResourceState}s.
 */
public interface ResourceStateListingService extends CachedDataListingService<ResourceState, ResourceStateId> {

    /**
     * Returns all the states that the dispatcher can manually assign a resource to, in the order that they should
     * appear in in the UI.
     */
    default @NotNull Stream<ResourceState> fetchDispatcherAssignable() {
        return fetchAll().filter(ResourceState::isDispatcherAssignable).sorted();
    }
}
