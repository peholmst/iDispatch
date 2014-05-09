package net.pkhsolutions.idispatch.boundary;

import net.pkhsolutions.idispatch.entity.ResourceType;
import net.pkhsolutions.idispatch.entity.repository.ResourceTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
class ResourceTypeManagementServiceBean extends AbstractSoftDeletableManagementServiceBean<ResourceType, ResourceTypeRepository> implements ResourceTypeManagementService {

    @Autowired
    ResourceTypeRepository repository;

    @Override
    protected ResourceTypeRepository getRepository() {
        return repository;
    }
}
