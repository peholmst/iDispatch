package net.pkhsolutions.idispatch.boundary;

import net.pkhsolutions.idispatch.entity.Destination;
import net.pkhsolutions.idispatch.entity.repository.DestinationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
class DestinationManagementServiceBean extends AbstractSoftDeletableManagementServiceBean<Destination, DestinationRepository> implements DestinationManagementService {

    @Autowired
    DestinationRepository repository;

    @Override
    protected DestinationRepository getRepository() {
        return repository;
    }
}
