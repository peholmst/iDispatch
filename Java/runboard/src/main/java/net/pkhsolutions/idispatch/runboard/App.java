package net.pkhsolutions.idispatch.runboard;


import net.pkhsolutions.idispatch.runboard.client.ServerPoller;

import javax.swing.*;

public class App {

    private MainView mainView;
    private ServerPoller poller;
    private Model model;

    private App() {
        final Configuration configuration = new Configuration();
        model = new Model();
        poller = new ServerPoller(configuration, model);
        mainView = new MainView(configuration.isLowResolution());
        mainView.setModel(model);
        mainView.setUndecorated(configuration.isUndecorated());
        mainView.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        model.addObserver(new Alarm());
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

    void start() {
        mainView.setVisible(true);
        poller.start();
    }
}
