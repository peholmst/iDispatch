package net.pkhsolutions.idispatch.ejb.resources;

import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import javax.annotation.security.RolesAllowed;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.OptimisticLockException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import net.pkhsolutions.idispatch.ejb.common.Roles;
import net.pkhsolutions.idispatch.ejb.masterdata.ResourceEJB;
import net.pkhsolutions.idispatch.entity.ArchivedResourceStatus;
import net.pkhsolutions.idispatch.entity.CurrentResourceStatus;
import net.pkhsolutions.idispatch.entity.Resource;
import net.pkhsolutions.idispatch.entity.ResourceState;
import net.pkhsolutions.idispatch.entity.Ticket;

@Stateless
@TransactionAttribute(TransactionAttributeType.REQUIRED)
@RolesAllowed(Roles.DISPATCHER)
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
            throw new NoStatusInformationFoundException(resource);
        }
    }

    public List<CurrentResourceStatus> getStatusOfAllActiveResources() {
        TypedQuery<CurrentResourceStatus> query = em.createQuery("SELECT crs FROM CurrentResourceStatus crs WHERE crs.resource.active = true", CurrentResourceStatus.class);
        return query.getResultList();
    }

    public List<ArchivedResourceStatus> getArchivedStatusesForTicket(Ticket ticket) {
        TypedQuery<ArchivedResourceStatus> query = em.createQuery("SELECT ars FROM ArchivedResourceStatus ars WHERE ars.ticket = :ticket ORDER BY ars.stateChangeTimestamp", ArchivedResourceStatus.class);
        query.setParameter("ticket", ticket);
        return query.getResultList();
    }

    public List<CurrentResourceStatus> getCurrentStatusesForTicket(Ticket ticket, ResourceState state) {
        TypedQuery<CurrentResourceStatus> query;
        if (state == null) {
            query = em.createQuery("SELECT crs FROM CurrentResourceStatus crs WHERE crs.ticket = :ticket", CurrentResourceStatus.class);
        } else {
            query = em.createQuery("SELECT crs FROM CurrentResourceStatus crs WHERE crs.ticket = :ticket AND crs.resourceState = :state", CurrentResourceStatus.class);
            query.setParameter("state", state);
        }
        query.setParameter("ticket", ticket);
        return query.getResultList();
    }

    public CurrentResourceStatus updateStatus(CurrentResourceStatus status) throws ResourceStatusChangedException {
        try {
            status = em.merge(status);
            status.setStateChangeTimestamp(Calendar.getInstance());
            archive(status);
            if (Arrays.asList(ResourceState.AT_STATION, ResourceState.UNAVAILABLE).contains(status.getResourceState())) {
                status.setTicket(null);
            }
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
