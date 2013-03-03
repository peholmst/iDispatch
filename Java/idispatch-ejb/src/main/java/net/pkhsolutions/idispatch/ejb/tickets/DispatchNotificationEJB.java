package net.pkhsolutions.idispatch.ejb.tickets;

import java.util.Calendar;
import java.util.Collection;
import java.util.List;
import javax.annotation.security.PermitAll;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import net.pkhsolutions.idispatch.entity.DispatchNotification;
import net.pkhsolutions.idispatch.entity.DispatchNotificationReceiver;

/**
 *
 * @author peholmst
 */
@Stateless
@TransactionAttribute(TransactionAttributeType.REQUIRED)
@PermitAll
public class DispatchNotificationEJB {

    @PersistenceContext
    EntityManager em;

    public List<DispatchNotification> findUnseenNotifications(String receiverId, String securityCode) throws NoSuchReceiverException {
        DispatchNotificationReceiver receiver = findReceiver(receiverId, securityCode);
        TypedQuery<DispatchNotification> query = em.createQuery("SELECT dn FROM DispatchNotification dn WHERE dn.dispatchTimestamp > :timestampLimit AND NOT EXISTS (SELECT dnr.notification FROM DispatchNotificationReceipt dnr WHERE dnr.receiver = :receiver)", DispatchNotification.class);
        Calendar timestampLimit = Calendar.getInstance();
        timestampLimit.add(Calendar.MINUTE, -30);
        query.setParameter("timestampLimit", timestampLimit.getTime());
        query.setParameter("receiver", receiver);
        return query.getResultList();
    }

    public void markNotificationsAsSeen(String receiverId, String securityCode, Collection<Long> notificationIds) throws NoSuchReceiverException {
        // TODO Implement me
    }

    private DispatchNotificationReceiver findReceiver(String receiverId, String securityCode) throws NoSuchReceiverException {
        TypedQuery<DispatchNotificationReceiver> query = em.createQuery("SELECT dnr FROM DispatchNotificationReceiver dnr WHERE dnr.active = true AND dnr.receiverId = :receiverId AND dnr.securityCode = :securityCode",
                DispatchNotificationReceiver.class);
        query.setParameter("receiverId", receiverId);
        query.setParameter("securityCode", securityCode);
        try {
            return query.getSingleResult();
        } catch (NoResultException ex) {
            throw new NoSuchReceiverException();
        }
    }
}
