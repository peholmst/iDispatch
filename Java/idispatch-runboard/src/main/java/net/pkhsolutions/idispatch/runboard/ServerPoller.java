package net.pkhsolutions.idispatch.runboard;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.pkhsolutions.idispatch.runboard.rest.DispatcherClient;
import net.pkhsolutions.idispatch.runboard.rest.DispatcherClientException;
import net.pkhsolutions.idispatch.runboard.rest.Notifications;

/**
 *
 * @author peholmst
 */
public class ServerPoller {

    private static final Logger LOG = Logger.getLogger(ServerPoller.class.getName());
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
    private Model model;
    private int pollIntervalMilliseconds;
    private DispatcherClient client;
    private Runnable checkForNotifications = new Runnable() {
        @Override
        public void run() {
            try {
                Notifications notifications = client.retrieveNotifications();
                if (notifications != null) {
                    LOG.log(Level.INFO, "Received notifications {0}", notifications);
                    model.addNotifications(notifications);
                }
                model.setErrorCode(null);
            } catch (DispatcherClientException ex) {
                model.setErrorCode(ex.getCode());
            }
        }
    };
    private ScheduledFuture<?> handle;

    public ServerPoller(DispatcherClient client, Model model, int pollIntervalMilliseconds) {
        this.client = client;
        this.model = model;
        this.pollIntervalMilliseconds = pollIntervalMilliseconds;
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
