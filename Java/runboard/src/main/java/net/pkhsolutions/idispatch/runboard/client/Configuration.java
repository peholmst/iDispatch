package net.pkhsolutions.idispatch.runboard.client;

public class Configuration {

    protected Configuration() {
    }

    public String getUrl() {
        String url = System.getProperty("idispatch.rest.url", "");
        return url;
    }

    public String getRunboardKey() {
        String receiverId = System.getProperty("idispatch.runboardKey", "");
        return receiverId;
    }

    public int getPollingIntervalMilliseconds() {
        String interval = System.getProperty("idispatch.pollingInterval", "1000");
        return Integer.parseInt(interval);
    }

}
