package net.pkhsolutions.idispatch.ejb.masterdata;

import java.util.List;
import java.util.logging.Logger;
import javax.annotation.security.PermitAll;
import javax.ejb.Stateless;
import net.pkhsolutions.idispatch.entity.Resource;

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
    public List<Resource> findAll() {
        return em().createQuery("SELECT r FROM Resource r ORDER BY r.callSign", Resource.class).getResultList();
    }

    public List<Resource> findActive() {
        return em().createQuery("SELECT r FROM Resource r WHERE r.active = true ORDER BY r.callSign", Resource.class).getResultList();
    }
}
