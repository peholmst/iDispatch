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
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import net.pkhsolutions.idispatch.entity.DispatchNotification;
import net.pkhsolutions.idispatch.entity.DispatchNotificationReceipt;
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
        TypedQuery<DispatchNotification> query = em.createQuery("SELECT dn FROM DispatchNotification dn WHERE dn.dispatchTimestamp > :timestampLimit AND NOT EXISTS (SELECT dnr.notification FROM DispatchNotificationReceipt dnr WHERE dnr.receiver = :receiver AND dnr.notification = dn)", DispatchNotification.class);
        Calendar timestampLimit = Calendar.getInstance();
        timestampLimit.add(Calendar.MINUTE, -5);
        query.setParameter("timestampLimit", timestampLimit.getTime());
        query.setParameter("receiver", receiver);
        return query.getResultList();
    }

    public void markNotificationsAsSeen(String receiverId, String securityCode, Collection<Long> notificationIds) throws NoSuchReceiverException {
        DispatchNotificationReceiver receiver = findReceiver(receiverId, securityCode);
        for (Long notificationId : notificationIds) {
            DispatchNotification notification = em.find(DispatchNotification.class, notificationId);
            if (notification != null && !hasBeenSeen(notification, receiver)) {
                DispatchNotificationReceipt receipt = new DispatchNotificationReceipt(receiver, notification);
                em.persist(receipt);
            }
        }
        em.flush();
    }

    private boolean hasBeenSeen(DispatchNotification notification, DispatchNotificationReceiver receiver) {
        Query query = em.createQuery("SELECT COUNT(dnr) FROM DispatchNotificationReceipt dnr WHERE dnr.notification = :notification AND dnr.receiver = :receiver");
        query.setParameter("notification", notification);
        query.setParameter("receiver", receiver);
        return ((Number) query.getSingleResult()).intValue() > 0;
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
