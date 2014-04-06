package net.pkhsolutions.idispatch.ejb.masterdata;

import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.logging.Logger;
import javax.annotation.security.RolesAllowed;
import javax.ejb.Stateless;
import javax.persistence.TypedQuery;
import net.pkhsolutions.idispatch.ejb.common.ConcurrentModificationException;
import net.pkhsolutions.idispatch.ejb.common.Roles;
import net.pkhsolutions.idispatch.ejb.common.SaveFailedException;
import net.pkhsolutions.idispatch.ejb.common.ValidationFailedException;
import net.pkhsolutions.idispatch.entity.ArchivedResourceStatus;
import net.pkhsolutions.idispatch.entity.CurrentResourceStatus;
import net.pkhsolutions.idispatch.entity.Resource;
import net.pkhsolutions.idispatch.entity.ResourceState;

/**
 * EJB for managing {@link Resource}s.
 *
 * @author Petter Holmström
 */
@Stateless
@RolesAllowed(Roles.ADMIN)
public class ResourceEJB extends Backend<Resource> {

    private static final Logger log = Logger.getLogger(ResourceEJB.class.getCanonicalName());

    @Override
    protected Logger log() {
        return log;
    }

    @Override
    public Resource save(Resource entity) throws ValidationFailedException, ConcurrentModificationException, SaveFailedException {
        boolean initializeStatusRecord = !entity.isPersistent();
        Resource resource = super.save(entity);
        if (initializeStatusRecord) {
            CurrentResourceStatus status = new CurrentResourceStatus.Builder()
                    .withResource(resource)
                    .withState(ResourceState.UNAVAILABLE)
                    .withTimestamp(Calendar.getInstance())
                    .build();
            em().persist(status);
            em().persist(new ArchivedResourceStatus.Builder(status).build());
            em().flush();
        }
        return resource;
    }

    @Override
    @RolesAllowed({Roles.ADMIN, Roles.DISPATCHER})
    public List<Resource> findAll() {
        return em().createQuery("SELECT r FROM Resource r ORDER BY r.callSign", Resource.class).getResultList();
    }

    @RolesAllowed({Roles.ADMIN, Roles.DISPATCHER})
    public List<Resource> findActive() {
        return em().createQuery("SELECT r FROM Resource r WHERE r.active = true ORDER BY r.callSign", Resource.class).getResultList();
    }

    @RolesAllowed({Roles.ADMIN, Roles.DISPATCHER})
    public List<Resource> findActiveAndAvailable() {
        TypedQuery<Resource> query = em().createQuery("SELECT crs.resource FROM CurrentResourceStatus crs WHERE crs.resource.active = TRUE AND crs.resourceState IN :states ORDER BY crs.resource.callSign", Resource.class);
        query.setParameter("states", Arrays.asList(ResourceState.AT_STATION, ResourceState.AVAILABLE));
        return query.getResultList();
    }
}
