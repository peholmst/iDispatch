package net.pkhapps.idispatch.cad.service.resource;

import net.pkhapps.idispatch.cad.domain.model.resource.Resource;
import net.pkhapps.idispatch.cad.domain.model.resource.ResourceId;
import net.pkhapps.idispatch.cad.domain.model.resource.ResourceRepository;
import net.pkhapps.idispatch.cad.domain.model.resource.ResourceTypeId;
import net.pkhapps.idispatch.cad.domain.model.station.StationId;
import net.pkhapps.idispatch.cad.infrastructure.security.AccessControlManager;
import net.pkhapps.idispatch.cad.infrastructure.tx.UnitOfWorkManager;

import javax.annotation.Nonnull;
import java.util.Objects;

/**
 * Application service for managing {@link Resource}s.
 */
public class ResourceManagementService {

    private final ResourceRepository resourceRepository;
    private final UnitOfWorkManager unitOfWorkManager;
    private final AccessControlManager accessControlManager;

    public ResourceManagementService(@Nonnull ResourceRepository resourceRepository,
                                     @Nonnull UnitOfWorkManager unitOfWorkManager,
                                     @Nonnull AccessControlManager accessControlManager) {
        this.resourceRepository = Objects.requireNonNull(resourceRepository, "resourceRepository must not be null");
        this.unitOfWorkManager = Objects.requireNonNull(unitOfWorkManager, "unitOfWorkManager must not be null");
        this.accessControlManager = Objects.requireNonNull(accessControlManager, "accessControlManager must not be null");
    }

    /**
     * Creates a new {@link Resource}.
     *
     * @param type        the type of the resource.
     * @param designation the designation of the resource.
     * @param stationedAt the station that the resource is stationed at.
     * @return the created resource.
     */
    @Nonnull
    public Resource create(@Nonnull ResourceTypeId type, @Nonnull String designation, @Nonnull StationId stationedAt) {
        accessControlManager.require(ResourceAuthority.CREATE_RESOURCE);
        return unitOfWorkManager.requireNew().execute(() -> {
            var resource = new Resource(resourceRepository.nextFreeId(), type, designation, stationedAt);
            resourceRepository.add(resource);
            return resource;
        });
    }

    /**
     * Deletes the specified resource.
     *
     * @param resourceId the ID of the resource to delete.
     */
    public void delete(@Nonnull ResourceId resourceId) {
        accessControlManager.require(ResourceAuthority.DELETE_RESOURCE);
        unitOfWorkManager.requireNew().execute(() -> resourceRepository.get(resourceId).ifPresent(Resource::deactivate));
    }
}
