package net.pkhsolutions.idispatch.ejb.resources;

import java.util.Calendar;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.OptimisticLockException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import net.pkhsolutions.idispatch.ejb.masterdata.ResourceEJB;
import net.pkhsolutions.idispatch.entity.ArchivedResourceStatus;
import net.pkhsolutions.idispatch.entity.CurrentResourceStatus;
import net.pkhsolutions.idispatch.entity.Resource;

@Stateless
@TransactionAttribute(TransactionAttributeType.REQUIRED)
// TODO Security
public class ResourceStatusEJB {

    @PersistenceContext
    EntityManager em;
    @EJB
    ResourceEJB resourceBean;

    public CurrentResourceStatus getCurrentStatus(Resource resource) throws NoStatusInformationFoundException {
        TypedQuery<CurrentResourceStatus> query = em.createQuery("SELECT crs FROM CurrentResourceStatus crs WHERE crs.resource = :resource", CurrentResourceStatus.class);
        query.setParameter("resource", resource);
        try {
            return query.getSingleResult();
        } catch (NoResultException e) {
            throw new NoStatusInformationFoundException();
        }
    }

    public List<CurrentResourceStatus> getStatusOfAllActiveResources() {
        TypedQuery<CurrentResourceStatus> query = em.createQuery("SELECT crs FROM CurrentResourceStatus crs WHERE crs.resource.active = true", CurrentResourceStatus.class);
        return query.getResultList();
    }

    public CurrentResourceStatus updateStatus(CurrentResourceStatus status) throws ResourceStatusChangedException {
        try {
            status = em.merge(status);
            status.setStateChangeTimestamp(Calendar.getInstance());
            archive(status);
            em.flush();
            return status;
        } catch (OptimisticLockException e) {
            throw new ResourceStatusChangedException();
        }
    }

    private void archive(CurrentResourceStatus status) {
        ArchivedResourceStatus archivedStatus = new ArchivedResourceStatus.Builder(status).build();
        em.persist(archivedStatus);
    }
}