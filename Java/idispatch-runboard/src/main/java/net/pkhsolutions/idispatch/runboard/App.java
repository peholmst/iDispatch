package net.pkhsolutions.idispatch.runboard;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import net.pkhsolutions.idispatch.runboard.rest.DispatcherClient;

public class App {

    private MainView mainView;
    private DispatcherClient client;
    private ServerPoller poller;
    private Model model;

    private App() {
        client = new DispatcherClient(Configuration.getUrl(),
                Configuration.getReceiverId(),
                Configuration.getSecurityCode(),
                Configuration.isVerifyingSslCertificate());
        model = new Model(Configuration.getConcernedResources());
        poller = new ServerPoller(client, model, Configuration.getPollingIntervalMilliseconds());
        mainView = new MainView(Configuration.getLanguage());
        mainView.setModel(model);
        mainView.setUndecorated(Configuration.isUndecorated());
        mainView.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        model.addObserver(new Alarm());
    }

    void start() {
        mainView.setVisible(true);
        poller.start();
    }

    public static void main(String[] args) throws Exception {
        UIManager.setLookAndFeel(
                UIManager.getCrossPlatformLookAndFeelClassName());
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new App().start();
            }
        });
    }
}
