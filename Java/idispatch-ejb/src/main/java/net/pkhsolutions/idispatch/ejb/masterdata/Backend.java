package net.pkhsolutions.idispatch.ejb.masterdata;

import net.pkhsolutions.idispatch.ejb.common.ConcurrentModificationException;
import net.pkhsolutions.idispatch.ejb.common.ValidationFailedException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.OptimisticLockException;
import javax.persistence.PersistenceContext;
import javax.validation.Validator;
import net.pkhsolutions.idispatch.entity.AbstractEntity;

/**
 *
 * @author peholmst
 */
public abstract class Backend<E extends AbstractEntity> {

    @PersistenceContext
    EntityManager em;
    @Resource
    Validator validator;

    protected EntityManager em() {
        return em;
    }

    protected Validator validator() {
        return validator;
    }

    protected abstract Logger log();

    public E refresh(E entity) {
        entity = em().merge(entity);
        em().refresh(entity);
        return entity;
    }

    public abstract List<E> findAll();

    public E save(E entity) throws ValidationFailedException, ConcurrentModificationException {
        log().log(Level.INFO, "Saving entity {0} with ID {1}",
                new Object[]{entity, entity.getId()});
        ValidationFailedException.throwIfNonEmpty(validator().validate(entity));
        try {
            if (entity.isPersistent()) {
                entity = em().merge(entity);
            } else {
                em().persist(entity);
            }
            em().flush();
            return entity;
        } catch (OptimisticLockException ex) {
            throw new ConcurrentModificationException();
        }
    }

    public void delete(E entity) throws ConcurrentModificationException {
        if (!entity.isPersistent()) {
            return;
        }
        log().log(Level.INFO, "Deleting entity {0} with ID {1}",
                new Object[]{entity, entity.getId()});
        try {
            em().remove(em().merge(entity));
            em().flush();
        } catch (OptimisticLockException e) {
            throw new ConcurrentModificationException();
        }
    }
}
