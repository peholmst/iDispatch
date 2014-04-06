package net.pkhsolutions.idispatch.ejb.masterdata;

import java.util.List;
import java.util.logging.Logger;
import javax.annotation.security.RolesAllowed;
import javax.ejb.Stateless;
import net.pkhsolutions.idispatch.entity.Municipality;

/**
 * EJB for managing {@link Municipality}-objects.
 *
 * @author Petter Holmström
 */
@Stateless
@RolesAllowed("ADMIN")
public class MunicipalityEJB extends Backend<Municipality> {

    private static final Logger log = Logger.getLogger(MunicipalityEJB.class.getCanonicalName());

    @Override
    protected Logger log() {
        return log;
    }

    @Override
    @RolesAllowed({"ADMIN", "DISPATCHER"})
    public List<Municipality> findAll() {
        return em().createQuery("SELECT m FROM Municipality m", Municipality.class).getResultList();
    }
}
