package net.pkhsolutions.idispatch.ejb.masterdata;

import java.util.List;
import java.util.logging.Logger;
import javax.annotation.security.RolesAllowed;
import javax.ejb.Stateless;
import net.pkhsolutions.idispatch.ejb.common.Roles;
import net.pkhsolutions.idispatch.entity.TicketType;

/**
 * EJB for managing {@link TicketType}s.
 *
 * @author Petter Holmström
 */
@Stateless
@RolesAllowed(Roles.ADMIN)
public class TicketTypeEJB extends Backend<TicketType> {

    private static final Logger log = Logger.getLogger(TicketTypeEJB.class.getCanonicalName());

    @Override
    @RolesAllowed({Roles.ADMIN, Roles.DISPATCHER})
    public List<TicketType> findAll() {
        return em().createQuery("SELECT tt FROM TicketType tt ORDER BY tt.code", TicketType.class).getResultList();
    }

    @Override
    protected Logger log() {
        return log;
    }
}
