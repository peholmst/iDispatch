package net.pkhsolutions.idispatch.runboard.rest;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientHandlerException;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.UniformInterfaceException;
import com.sun.jersey.api.client.WebResource;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import javax.ws.rs.core.MediaType;

public class DispatcherClient {

    private static final Logger LOG = Logger.getLogger(DispatcherClient.class.getName());
    private Client client = Client.create();
    private WebResource dispatcher;
    private String receiverId;
    private String securityCode;

    public DispatcherClient(String url, String receiverId, String securityCode, boolean verifySslCertificate) {
        this.receiverId = receiverId;
        this.securityCode = securityCode;
        client = Client.create();
        if (url.endsWith("/")) {
            dispatcher = client.resource(url + "dispatcher");
        } else {
            dispatcher = client.resource(url + "/dispatcher");
        }
        if (!verifySslCertificate) {
            TrustManager[] trustAllCerts = new TrustManager[]{
                new X509TrustManager() {
                    @Override public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                        return null;
                    }

                    @Override public void checkClientTrusted(X509Certificate[] certs, String authType) {
                    }

                    @Override public void checkServerTrusted(X509Certificate[] certs, String authType) {
                    }
                }
            };
            try {
                SSLContext sc = SSLContext.getInstance("SSL");
                sc.init(null, trustAllCerts, new java.security.SecureRandom());
                HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
            } catch (KeyManagementException | NoSuchAlgorithmException ex) {
                LOG.log(Level.WARNING, "Error installing all accepting trust manager", ex);
            }
        }
    }

    public Notifications retrieveNotifications() throws DispatcherClientException {
        try {
            ClientResponse response = dispatcher
                    .queryParam("id", receiverId)
                    .queryParam("sc", securityCode)
                    .accept(MediaType.APPLICATION_XML)
                    .get(ClientResponse.class);

            switch (response.getStatus()) {
                case 403: // Forbidden
                    throw new DispatcherClientException(DispatcherClientException.ErrorCode.INVALID_CREDENTIALS);
                case 204: // No content
                    return null;
                case 200: // OK
                    // TODO Mark notifications as read
                    return response.getEntity(Notifications.class);
                default:
                    throw new DispatcherClientException(DispatcherClientException.ErrorCode.UNKNOWN_SERVER_ERROR);
            }
        } catch (UniformInterfaceException | ClientHandlerException e) {
            throw new DispatcherClientException(DispatcherClientException.ErrorCode.COMMUNICATION_ERROR, e);
        }
    }

    public static void main(String[] args) {
        // For testing only
        DispatcherClient client = new DispatcherClient("https://localhost:8181/idispatch-rest", "gsm", "6ffbqnv4LZDQrOA", false);
        try {
            System.out.println(client.retrieveNotifications());
        } catch (DispatcherClientException ex) {
            System.out.println("Error code: " + ex.getCode().name());
        }
    }
}
