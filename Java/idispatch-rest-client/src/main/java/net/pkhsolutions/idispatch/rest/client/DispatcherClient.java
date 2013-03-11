package net.pkhsolutions.idispatch.rest.client;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientHandlerException;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.UniformInterfaceException;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.core.util.MultivaluedMapImpl;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;

public class DispatcherClient {

    private static final Logger LOG = Logger.getLogger(DispatcherClient.class.getName());
    private Client client = Client.create();
    private WebResource dispatcher;
    private String receiverId;
    private String securityCode;
    private Configuration configuration;

    public DispatcherClient(Configuration configuration) {
        this.configuration = configuration;
        this.receiverId = configuration.getReceiverId();
        this.securityCode = configuration.getSecurityCode();
        client = Client.create();
        if (configuration.getUrl().endsWith("/")) {
            dispatcher = client.resource(configuration.getUrl() + "dispatcher");
        } else {
            dispatcher = client.resource(configuration.getUrl() + "/dispatcher");
        }
        if (!configuration.isVerifyingSslCertificate()) {
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
            HostnameVerifier trustAllhosts = new HostnameVerifier() {
                @Override
                public boolean verify(String string, SSLSession ssls) {
                    return true;
                }
            };
            try {
                SSLContext sc = SSLContext.getInstance("SSL");
                sc.init(null, trustAllCerts, new java.security.SecureRandom());
                HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
                HttpsURLConnection.setDefaultHostnameVerifier(trustAllhosts);
            } catch (KeyManagementException | NoSuchAlgorithmException ex) {
                LOG.log(Level.WARNING, "Error installing all accepting trust manager", ex);
            }
        }
    }

    public Configuration getConfiguration() {
        return configuration;
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
                    Notifications result = response.getEntity(Notifications.class);
                    markAsSeen(result);
                    return result;
                default:
                    LOG.log(Level.WARNING, "Unexpected server status code: {0}", response.getStatus());
                    throw new DispatcherClientException(DispatcherClientException.ErrorCode.UNKNOWN_SERVER_ERROR);
            }
        } catch (UniformInterfaceException | ClientHandlerException e) {
            LOG.log(Level.SEVERE, "Communication error", e);
            throw new DispatcherClientException(DispatcherClientException.ErrorCode.COMMUNICATION_ERROR, e);
        }
    }

    private void markAsSeen(Notifications result) {
        MultivaluedMap formData = new MultivaluedMapImpl();
        formData.add("id", receiverId);
        formData.add("sc", securityCode);

        StringBuilder ids = new StringBuilder();
        for (Iterator<Notification> it = result.getNotifications().iterator(); it.hasNext();) {
            ids.append(it.next().getId());
            if (it.hasNext()) {
                ids.append(":");
            }
        }
        formData.add("ids", ids.toString());
        dispatcher.type(MediaType.APPLICATION_FORM_URLENCODED_TYPE).post(formData);
    }
}
