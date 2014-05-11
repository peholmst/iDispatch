package net.pkhsolutions.idispatch.runboard;

import net.pkhsolutions.idispatch.rest.client.DispatcherClientException;
import net.pkhsolutions.idispatch.rest.client.Notification;
import net.pkhsolutions.idispatch.rest.client.Notifications;
import net.pkhsolutions.idispatch.rest.client.ServerPoller;

import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Model extends Observable implements ServerPoller.Callback {

    private static final Logger LOG = Logger.getLogger(Model.class.getName());
    private final List<Notification> notifications = new ArrayList<>();
    private final Set<Long> seenNotifications = new HashSet<>();
    private final Set<String> concernedResources;
    private final Timer expirationTimer = new Timer();
    private DispatcherClientException.ErrorCode errorCode;

    public Model(Set<String> concernedResources) {
        if (LOG.isLoggable(Level.INFO)) {
            for (String r : concernedResources) {
                LOG.log(Level.INFO, "Watching for notifications to {0}", r);
            }
        }
        this.concernedResources = concernedResources;
        expirationTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                removeExpiredNotifications();
            }
        }, 1000, 30000);
    }

    private synchronized void removeExpiredNotifications() {
        Calendar now = Calendar.getInstance();
        for (Iterator<Notification> it = notifications.iterator(); it.hasNext(); ) {
            Notification notification = it.next();
            if (isExpired(now, notification)) {
                LOG.log(Level.INFO, "Removing expired notification {0}", notification);
                it.remove();
                seenNotifications.remove(notification.getId());
                setChanged();
            }
        }
        notifyObservers();
    }

    private boolean isExpired(Calendar now, Notification notification) {
        Calendar expirationDate = (Calendar) notification.getTimestamp().clone();
        expirationDate.add(Calendar.MINUTE, 3);
        return now.after(expirationDate);
    }

    public synchronized DispatcherClientException.ErrorCode getErrorCode() {
        return errorCode;
    }

    @Override
    public synchronized void setErrorCode(DispatcherClientException.ErrorCode errorCode) {
        if (!Objects.equals(this.errorCode, errorCode)) {
            LOG.log(Level.INFO, "Setting error code to {0}", errorCode);
            this.errorCode = errorCode;
            setChanged();
        }
        notifyObservers();
    }

    public synchronized boolean hasError() {
        return errorCode != null;
    }

    private void addNotifications(Notifications notifications) {
        for (Iterator<Notification> it = notifications.getNotificationsForResources(concernedResources).iterator(); it.hasNext(); ) {
            Notification notificationToAdd = it.next();
            if (!seenNotifications.contains(notificationToAdd.getId())) {
                LOG.log(Level.INFO, "Adding notification {0} to model", notificationToAdd);
                this.notifications.add(notificationToAdd);
                seenNotifications.add(notificationToAdd.getId());
                setChanged();
            }
        }
        notifyObservers();
    }

    public synchronized List<Notification> getVisibleNotifications() {
        return Collections.unmodifiableList(notifications);
    }

    @Override
    public void clearErrorCode() {
        setErrorCode(null);
    }

    @Override
    public synchronized void notificationsReceived(Notifications notifications) {
        addNotifications(notifications);
    }
}
