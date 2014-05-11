package net.pkhsolutions.idispatch.runboard.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class ServerPoller {

    private final Logger logger = LoggerFactory.getLogger(getClass());
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
    private int pollIntervalMilliseconds;
    private RunboardRestClient client;
    private Callback callback;
    private ScheduledFuture<?> handle;
    private int errorCount = 0;
    private int skipCount = 0;

    public ServerPoller(Configuration configuration, Callback callback) {
        this.client = new RunboardRestClient(configuration);
        this.callback = callback;
        this.pollIntervalMilliseconds = configuration.getPollingIntervalMilliseconds();
    }

    public synchronized void start() {
        if (!isRunning()) {
            handle = scheduler.scheduleWithFixedDelay(this::pollServer,
                    pollIntervalMilliseconds,
                    pollIntervalMilliseconds,
                    TimeUnit.MILLISECONDS);
        }
    }

    public synchronized void stop() {
        if (handle != null && handle.cancel(false)) {
            handle = null;
        }
    }

    private synchronized void pollServer() {
        try {
            if (skipCount == 0) {
                final List<Notification> notifications = client.findNewNotifications().stream().map(map -> new Notification(map)).collect(Collectors.toList());
                if (notifications.size() > 0) {
                    logger.info("Received notifications {}", notifications);
                    callback.notificationsReceived(notifications);
                }
                errorCount = 0;
                skipCount = 0;
                callback.clearErrorCode();
            } else {
                logger.debug("Skipping {} polls due to previous communication errors", skipCount);
                skipCount--;
            }
        } catch (DispatcherClientException ex) {
            logger.error("Error while polling the server: {}", ex.getCode());
            callback.setErrorCode(ex.getCode());
            if (errorCount < 60) {
                errorCount++;
            }
            skipCount = errorCount;
        }
    }

    public synchronized boolean isRunning() {
        return handle != null && !handle.isDone();
    }

    public interface Callback {

        void clearErrorCode();

        void setErrorCode(DispatcherClientException.ErrorCode errorCode);

        void notificationsReceived(Collection<Notification> notifications);
    }
}
