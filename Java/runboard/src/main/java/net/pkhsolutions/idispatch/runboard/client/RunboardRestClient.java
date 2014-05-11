package net.pkhsolutions.idispatch.runboard.client;

import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

public class RunboardRestClient {

    private Configuration configuration;
    private RestTemplate restTemplate;

    public RunboardRestClient(Configuration configuration) {
        this.configuration = configuration;
        restTemplate = new RestTemplate();
    }

    public Configuration getConfiguration() {
        return configuration;
    }

    public List<Map<String, Object>> findNewNotifications() throws DispatcherClientException {
        /*
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
        }  */
        return null;
    }

    private void acknowledge(Long notificationId) {

    }
}
