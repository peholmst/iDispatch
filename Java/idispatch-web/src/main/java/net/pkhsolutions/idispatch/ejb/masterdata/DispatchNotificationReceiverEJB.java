package net.pkhsolutions.idispatch.ejb.masterdata;

import java.util.List;
import java.util.logging.Logger;
import javax.annotation.security.RolesAllowed;
import javax.ejb.Stateless;
import net.pkhsolutions.idispatch.ejb.common.Roles;
import net.pkhsolutions.idispatch.entity.DispatchNotificationReceiver;

/**
 * EJB for managing {@link DispatchNotificationReceiver}s.
 *
 * @author Petter Holmström
 */
@Stateless
@RolesAllowed(Roles.ADMIN)
public class DispatchNotificationReceiverEJB extends Backend<DispatchNotificationReceiver> {

    private static final Logger log = Logger.getLogger(DispatchNotificationReceiverEJB.class.getCanonicalName());

    @Override
    public List<DispatchNotificationReceiver> findAll() {
        return em().createQuery("SELECT dnr FROM DispatchNotificationReceiver dnr ORDER BY dnr.receiverId", DispatchNotificationReceiver.class).getResultList();
    }

    public List<DispatchNotificationReceiver> findActive() {
        return em().createQuery("SELECT dnr FROM DispatchNotificationReceiver dnr WHERE dnr.active = true ORDER BY dnr.receiverId", DispatchNotificationReceiver.class).getResultList();
    }

    @Override
    protected Logger log() {
        return log;
    }
}
