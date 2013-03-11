package net.pkhsolutions.idispatch.runboard;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import net.pkhsolutions.idispatch.rest.client.DispatcherClient;
import net.pkhsolutions.idispatch.rest.client.ServerPoller;

public class App {

    private MainView mainView;
    private DispatcherClient client;
    private ServerPoller poller;
    private Model model;

    private App() {
        Configuration configuration = new Configuration();
        client = new DispatcherClient(configuration);
        model = new Model(configuration.getConcernedResources());
        poller = new ServerPoller(client, model);
        mainView = new MainView(configuration.getLanguage(), configuration.isLowResolution());
        mainView.setModel(model);
        mainView.setUndecorated(configuration.isUndecorated());
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
