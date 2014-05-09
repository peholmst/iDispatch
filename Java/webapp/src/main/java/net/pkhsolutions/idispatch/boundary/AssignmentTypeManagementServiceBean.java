package net.pkhsolutions.idispatch.boundary;

import net.pkhsolutions.idispatch.entity.AssignmentType;
import net.pkhsolutions.idispatch.entity.repository.AssignmentTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
class AssignmentTypeManagementServiceBean extends AbstractSoftDeletableManagementServiceBean<AssignmentType, AssignmentTypeRepository> implements AssignmentTypeManagementService {

    @Autowired
    AssignmentTypeRepository repository;

    @Override
    protected AssignmentTypeRepository getRepository() {
        return repository;
    }
}
