package net.pkhsolutions.idispatch.runboard;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import javax.swing.JTabbedPane;
import javax.swing.Timer;
import net.pkhsolutions.idispatch.runboard.rest.DispatcherClientException;
import net.pkhsolutions.idispatch.runboard.rest.Notification;

public class MainView extends JFrame implements Observer {
    private static final Logger LOG = Logger.getLogger(MainView.class.getName());
    private final Timer cardFlipperTimer;
    private final Map<Notification, NotificationView> views = new HashMap<>();
    private final Language language;
    private Model model;
    private JTabbedPane notifications;

    public MainView(Language language) {
        super("iDispatch Runboard");
        setExtendedState(MAXIMIZED_BOTH);
        this.language = language;
        notifications = new JTabbedPane();
        add(notifications, BorderLayout.CENTER);
        cardFlipperTimer = new Timer(10000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                flipCard();
            }
        });
        cardFlipperTimer.start();
    }

    private void flipCard() {
        if (notifications.getTabCount() > 0) {
            notifications.setSelectedIndex((notifications.getSelectedIndex() + 1) % notifications.getTabCount());
        }
    }

    public void setModel(Model model) {
        if (this.model != null) {
            this.model.deleteObserver(this);
        }
        this.model = model;
        if (this.model != null) {
            this.model.addObserver(this);
        }
        update(model, null);
    }

    private void showErrorMessage(DispatcherClientException.ErrorCode errorCode) {
        // TODO Implement me
        System.out.println(errorCode);
    }

    private void clearErrorMessage() {
        // TODO Implement me
    }

    @Override
    public void update(Observable o, Object arg) {
        LOG.info("Model updated");
        if (model != null && model.hasError()) {
            showErrorMessage(model.getErrorCode());
        } else {
            clearErrorMessage();
        }
        Set<Notification> notificationsToRemove = new HashSet<>(views.keySet());
        if (model != null) {
            for (Notification n : model.getVisibleNotifications()) {
                if (!notificationsToRemove.remove(n)) {
                    addCard(n);
                }
            }
        }
        for (Notification n : notificationsToRemove) {
            removeCard(n);
        }
    }

    private void removeCard(Notification notification) {
        LOG.log(Level.INFO, "Removing card for notification {0}", notification);
        NotificationView view = views.remove(notification);
        notifications.remove(view);
    }

    private void addCard(Notification notification) {
        LOG.log(Level.INFO, "Adding card for notification {0}", notification);
        NotificationView view = new NotificationView(notification, language);
        views.put(notification, view);
        notifications.addTab(String.format("Ticket %d (Notification %d)", notification.getTicketId(), notification.getId()), view);
    }

}
