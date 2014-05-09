package net.pkhsolutions.idispatch.boundary;

import net.pkhsolutions.idispatch.entity.AbstractEntity;
import net.pkhsolutions.idispatch.entity.Deactivatable;

import java.util.List;

public interface ManagementService<E extends AbstractEntity> {

    E save(E entity);

    List<E> findAll();

    interface HardDeletable<E extends AbstractEntity> extends ManagementService<E> {
        boolean delete(E entity);
    }

    interface SoftDeletable<E extends AbstractEntity & Deactivatable> extends ManagementService<E> {

        void delete(E entity);

        void restore(E entity);
    }
}
