package net.pkhsolutions.idispatch.boundary;

import net.pkhsolutions.idispatch.entity.AbstractEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

abstract class AbstractManagementServiceBean<E extends AbstractEntity, R extends JpaRepository<E, Long>> implements ManagementService<E> {

    protected final Logger logger = LoggerFactory.getLogger(getClass());

    protected abstract R getRepository();

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public E save(E entity) {
        logger.debug("Saving entity {}", entity);
        return getRepository().saveAndFlush(entity);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = true)
    public List<E> findAll() {
        logger.debug("Finding all entities");
        return getRepository().findAll();
    }
}
