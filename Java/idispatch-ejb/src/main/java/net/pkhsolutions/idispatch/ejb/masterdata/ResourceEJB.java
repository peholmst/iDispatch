package net.pkhsolutions.idispatch.ejb.masterdata;

import java.util.Calendar;
import java.util.List;
import java.util.logging.Logger;
import javax.annotation.security.PermitAll;
import javax.ejb.Stateless;
import net.pkhsolutions.idispatch.ejb.common.ConcurrentModificationException;
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
@PermitAll // TODO Replace with admin role
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
    public List<Resource> findAll() {
        return em().createQuery("SELECT r FROM Resource r ORDER BY r.callSign", Resource.class).getResultList();
    }

    public List<Resource> findActive() {
        return em().createQuery("SELECT r FROM Resource r WHERE r.active = true ORDER BY r.callSign", Resource.class).getResultList();
    }
}
