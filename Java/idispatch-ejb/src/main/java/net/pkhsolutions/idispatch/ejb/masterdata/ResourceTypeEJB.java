package net.pkhsolutions.idispatch.ejb.masterdata;

import java.util.List;
import java.util.logging.Logger;
import javax.annotation.security.RolesAllowed;
import javax.ejb.Stateless;
import net.pkhsolutions.idispatch.ejb.common.Roles;
import net.pkhsolutions.idispatch.entity.ResourceType;

/**
 * EJB for managing {@link ResourceType}s.
 *
 * @author Petter Holmström
 */
@Stateless
@RolesAllowed(Roles.ADMIN)
public class ResourceTypeEJB extends Backend<ResourceType> {

    private static final Logger log = Logger.getLogger(ResourceTypeEJB.class.getCanonicalName());

    @Override
    protected Logger log() {
        return log;
    }

    @Override
    @RolesAllowed({Roles.ADMIN, Roles.DISPATCHER})
    public List<ResourceType> findAll() {
        return em().createQuery("SELECT rt FROM ResourceType rt", ResourceType.class).getResultList();
    }
}
