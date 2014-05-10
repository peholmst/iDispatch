package net.pkhsolutions.idispatch.boundary;

import net.pkhsolutions.idispatch.entity.Resource;
import net.pkhsolutions.idispatch.entity.ResourceState;
import net.pkhsolutions.idispatch.entity.ResourceStatus;
import net.pkhsolutions.idispatch.entity.ValidationFailedException;
import net.pkhsolutions.idispatch.entity.repository.ResourceRepository;
import net.pkhsolutions.idispatch.entity.repository.ResourceStatusRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
class ResourceManagementServiceBean extends AbstractSoftDeletableManagementServiceBean<Resource, ResourceRepository> implements ResourceManagementService {

    @Autowired
    ResourceRepository repository;

    @Autowired
    ResourceStatusRepository resourceStatusRepository;

    @Override
    protected ResourceRepository getRepository() {
        return repository;
    }

    @Override
    public Resource save(Resource entity) throws ValidationFailedException {
        if (entity.isNew()) {
            final Resource saved = super.save(entity);
            logger.debug("Creating new status entry for resource {}", saved);
            final ResourceStatus resourceStatus = new ResourceStatus(saved, ResourceState.OUT_OF_SERVICE);
            resourceStatusRepository.saveAndFlush(resourceStatus);
            return saved;
        } else {
            return super.save(entity);
        }
    }
}
