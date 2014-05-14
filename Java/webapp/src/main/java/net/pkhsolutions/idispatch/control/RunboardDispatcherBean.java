package net.pkhsolutions.idispatch.control;

import net.pkhsolutions.idispatch.boundary.events.DispatchNotificationSent;
import net.pkhsolutions.idispatch.entity.DispatchNotification;
import net.pkhsolutions.idispatch.entity.RunboardDestination;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
class RunboardDispatcherBean extends AbstractDispatcher implements RunboardDispatcher {

    private final Map<String, RunboardDestination> runboardKeyMap = new HashMap<>();
    private final Map<RunboardDestination, Map<Long, DispatchNotification>> waitingNotifications = new HashMap<>();

    @Override
    @Async
    public void onApplicationEvent(DispatchNotificationSent event) {
        final DispatchNotification notification = event.getDispatchNotification();
        final Set<RunboardDestination> runboards = notification.getDestinationsOfType(RunboardDestination.class);
        runboards.forEach(runboard -> {
            final Map<Long, DispatchNotification> notifications = getOrCreateDispatchNotificationSet(runboard);
            logger.debug("Adding dispatch notification {} for destination {}", notification, runboard);
            synchronized (notifications) {
                notifications.put(notification.getId(), notification);
            }
        });
    }

    private Map<Long, DispatchNotification> getOrCreateDispatchNotificationSet(RunboardDestination destination) {
        synchronized (runboardKeyMap) {
            runboardKeyMap.put(destination.getRunboardKey(), destination);
        }
        synchronized (waitingNotifications) {
            Map<Long, DispatchNotification> notificationMap = waitingNotifications.get(destination);
            if (notificationMap == null) {
                notificationMap = new HashMap<>();
                waitingNotifications.put(destination, notificationMap);
            }
            return notificationMap;
        }
    }

    @Override
    public Collection<DispatchNotification> getDispatchNotifications(String runboardKey) {
        logger.trace("Looking up dispatch notifications for runboard {}", runboardKey);
        Optional<RunboardDestination> runboard = getDestinationByKey(runboardKey);
        if (runboard.isPresent()) {
            return getDispatchNotifications(runboard.get());
        }
        return Collections.emptyList();
    }

    private Optional<RunboardDestination> getDestinationByKey(String runboardKey) {
        synchronized (runboardKeyMap) {
            return Optional.ofNullable(runboardKeyMap.get(runboardKey));
        }
    }

    private Collection<DispatchNotification> getDispatchNotifications(RunboardDestination destination) {
        Map<Long, DispatchNotification> result;
        synchronized (waitingNotifications) {
            result = waitingNotifications.getOrDefault(destination, new HashMap<>());
        }
        synchronized (result) {
            return new HashSet<>(result.values());
        }
    }

    private DispatchNotification popDispatchNotification(RunboardDestination destination, Long id) {
        Map<Long, DispatchNotification> result;
        synchronized (waitingNotifications) {
            result = waitingNotifications.getOrDefault(destination, new HashMap<>());
        }
        synchronized (result) {
            return result.remove(id);
        }
    }

    @Override
    @Async
    public void acknowledgeDispatchNotification(String runboardKey, Long notificationId) {
        logger.debug("Acknowledging notification {} for runboard {}", notificationId, runboardKey);
        getDestinationByKey(runboardKey).ifPresent(runboard -> {
            DispatchNotification notification = popDispatchNotification(runboard, notificationId);
            if (notification != null) {
                createReceipt(runboard, notification);
            }
        });
    }

    @Override
    @Scheduled(fixedDelay = 60000)
    public void cleanUp() {
        logger.trace("Cleaning up expired dispatch notifications");
        Collection<Map<Long, DispatchNotification>> notificationMaps;
        synchronized (waitingNotifications) {
            notificationMaps = new HashSet<>(waitingNotifications.values());
        }
        notificationMaps.forEach(map -> {
            synchronized (map) {
                Iterator<DispatchNotification> iterator = map.values().iterator();
                while (iterator.hasNext()) {
                    DispatchNotification notification = iterator.next();
                    if (isExpired(notification)) {
                        logger.debug("Removing expired notification {}", notification);
                        iterator.remove();
                    }
                }
            }
        });
    }

    private boolean isExpired(DispatchNotification notification) {
        return notification.getTimestamp().getTime() < System.currentTimeMillis() - 120000;
    }

}
