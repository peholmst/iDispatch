package net.pkhsolutions.idispatch.runboard;

import net.pkhsolutions.idispatch.runboard.client.DispatcherClientException;
import net.pkhsolutions.idispatch.runboard.client.Notification;
import net.pkhsolutions.idispatch.runboard.client.ServerPoller;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;

class Model extends Observable implements ServerPoller.Callback {

    private final Logger logger = LoggerFactory.getLogger(getClass());
    private final List<Notification> notifications = new ArrayList<>();
    private final Set<Number> seenNotifications = new HashSet<>();
    private final Timer expirationTimer = new Timer();
    private DispatcherClientException.ErrorCode errorCode;

    Model() {
        expirationTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                removeExpiredNotifications();
            }
        }, 1000, 30000);
    }

    private synchronized void removeExpiredNotifications() {
        final Instant now = Instant.now();
        for (Iterator<Notification> it = notifications.iterator(); it.hasNext(); ) {
            Notification notification = it.next();
            if (isExpired(now, notification)) {
                logger.info("Removing expired notification {}", notification);
                it.remove();
                seenNotifications.remove(notification.getId());
                setChanged();
            }
        }
        notifyObservers();
    }

    private boolean isExpired(Instant now, Notification notification) {
        return notification.getTimestamp().toInstant().plus(3, ChronoUnit.MINUTES).isBefore(now);
    }

    public synchronized DispatcherClientException.ErrorCode getErrorCode() {
        return errorCode;
    }

    @Override
    public synchronized void setErrorCode(DispatcherClientException.ErrorCode errorCode) {
        if (!Objects.equals(this.errorCode, errorCode)) {
            logger.info("Setting error code to {}", errorCode);
            this.errorCode = errorCode;
            setChanged();
        }
        notifyObservers();
    }

    public synchronized boolean hasError() {
        return errorCode != null;
    }

    private void addNotifications(Collection<Notification> notifications) {
        notifications.forEach(this::addNotification);
        notifyObservers();
    }

    private void addNotification(Notification notification) {
        if (!seenNotifications.contains(notification.getId())) {
            logger.info("Adding notification {} to model", notification);
            notifications.add(notification);
            seenNotifications.add(notification.getId());
            setChanged();
        }
    }

    public synchronized List<Notification> getVisibleNotifications() {
        return Collections.unmodifiableList(notifications);
    }

    @Override
    public void clearErrorCode() {
        setErrorCode(null);
    }

    @Override
    public synchronized void notificationsReceived(Collection<Notification> notifications) {
        addNotifications(notifications);
    }
}
