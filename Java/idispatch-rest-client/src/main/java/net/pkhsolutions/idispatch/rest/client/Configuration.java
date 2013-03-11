package net.pkhsolutions.idispatch.rest.client;

public class Configuration {

    protected Configuration() {
    }

    public String getUrl() {
        String url = System.getProperty("idispatch.rest.url", "");
        return url;
    }

    public String getReceiverId() {
        String receiverId = System.getProperty("idispatch.receiverId", "");
        return receiverId;
    }

    public String getSecurityCode() {
        String securityCode = System.getProperty("idispatch.securityCode", "");
        return securityCode;
    }

    public int getPollingIntervalMilliseconds() {
        String interval = System.getProperty("idispatch.pollingInterval", "3000");
        return Integer.parseInt(interval);
    }

    public boolean isVerifyingSslCertificate() {
        String value = System.getProperty("idispatch.ssl.verify", "true");
        return Boolean.parseBoolean(value);
    }
}
