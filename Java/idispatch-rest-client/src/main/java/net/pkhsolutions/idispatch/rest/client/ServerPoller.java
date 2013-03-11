package net.pkhsolutions.idispatch.rest.client;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ServerPoller {

    public interface Callback {

        void clearErrorCode();

        void setErrorCode(DispatcherClientException.ErrorCode errorCode);

        void notificationsReceived(Notifications notifications);
    }
    private static final Logger LOG = Logger.getLogger(ServerPoller.class.getName());
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
    private int pollIntervalMilliseconds;
    private DispatcherClient client;
    private Callback callback;
    private Runnable checkForNotifications = new Runnable() {
        @Override
        public void run() {
            try {
                Notifications notifications = client.retrieveNotifications();
                if (notifications != null) {
                    LOG.log(Level.INFO, "Received notifications {0}", notifications);
                    callback.notificationsReceived(notifications);
                }
                callback.clearErrorCode();
            } catch (DispatcherClientException ex) {
                callback.setErrorCode(ex.getCode());
            }
        }
    };
    private ScheduledFuture<?> handle;

    public ServerPoller(DispatcherClient client, Callback callback) {
        this.client = client;
        this.callback = callback;
        this.pollIntervalMilliseconds = client.getConfiguration().getPollingIntervalMilliseconds();
    }

    public void start() {
        if (!isRunning()) {
            handle = scheduler.scheduleWithFixedDelay(checkForNotifications,
                    pollIntervalMilliseconds,
                    pollIntervalMilliseconds,
                    TimeUnit.MILLISECONDS);
        }
    }

    public void stop() {
        if (handle != null && handle.cancel(false)) {
            handle = null;
        }
    }

    public boolean isRunning() {
        return handle != null && !handle.isDone();
    }
}
