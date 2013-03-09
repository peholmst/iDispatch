package net.pkhsolutions.idispatch.runboard;

import java.awt.BorderLayout;
import java.awt.Color;
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
import javax.swing.BorderFactory;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
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
    private JDialog errorDialog;

    private void showErrorMessage(DispatcherClientException.ErrorCode errorCode) {
        if (errorDialog != null) {
            clearErrorMessage();
        }

        System.out.println(errorCode);
        JPanel errorPanel = new JPanel();
        errorPanel.setBackground(Color.RED);
        errorPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        JLabel errorLabel = new JLabel(errorCode.name());
        errorLabel.setFont(errorPanel.getFont().deriveFont(20.0f));
        errorPanel.add(errorLabel);

        errorDialog = new JDialog(this);
        errorDialog.setAlwaysOnTop(true);
        errorDialog.setModal(false);
        errorDialog.setUndecorated(true);
        errorDialog.add(errorPanel);
        errorDialog.pack();
        errorDialog.setLocationRelativeTo(this);
        errorDialog.setVisible(true);
    }

    private void clearErrorMessage() {
        if (errorDialog != null) {
            errorDialog.setVisible(false);
            errorDialog.dispose();
            errorDialog = null;
        }
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
