package net.pkhsolutions.idispatch.boundary;

import net.pkhsolutions.idispatch.entity.Municipality;
import net.pkhsolutions.idispatch.entity.repository.MunicipalityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
class MunicipalityManagementServiceBean extends AbstractSoftDeletableManagementServiceBean<Municipality, MunicipalityRepository> implements MunicipalityManagementService {

    @Autowired
    MunicipalityRepository repository;

    @Override
    protected MunicipalityRepository getRepository() {
        return repository;
    }
}
