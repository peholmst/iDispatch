package net.pkhapps.idispatch.core.client.resources;

import net.pkhapps.idispatch.core.client.CachedDataListingService;
import org.jetbrains.annotations.NotNull;

import java.util.stream.Stream;

/**
 * Application service for listing and looking up {@link ResourceType}s.
 */
public interface ResourceTypeListingService extends CachedDataListingService<ResourceType, ResourceTypeId> {

    /**
     * Fetches all resource types, optionally including types that are inactive.
     *
     * @param includeInactive true to include inactive types, false to only include active ones.
     * @return a stream of resource types.
     */
    @NotNull Stream<ResourceType> fetchAll(boolean includeInactive);
}
