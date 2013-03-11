package net.pkhsolutions.idispatch.smsgateway;

import net.pkhsolutions.idispatch.rest.client.DispatcherClient;
import net.pkhsolutions.idispatch.rest.client.ServerPoller;

public class App {

    private DispatcherClient client;
    private ServerPoller poller;
    private SmsSender sender;

    private App() throws Exception {
        Configuration configuration = new Configuration();
        client = new DispatcherClient(configuration);
        sender = new SmsSender(configuration);
        poller = new ServerPoller(client, sender);
    }

    void start() throws Exception {
        sender.connectToModem();
        poller.start();

        System.in.read();

        poller.stop();
        sender.disconnectFromModem();
        System.exit(0);
    }

    public static void main(String[] args) throws Exception {
        new App().start();
    }
}
