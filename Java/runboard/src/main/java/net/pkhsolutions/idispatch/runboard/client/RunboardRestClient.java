package net.pkhsolutions.idispatch.runboard.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Map;

class RunboardRestClient {

    private final Logger logger = LoggerFactory.getLogger(getClass());
    private Configuration configuration;
    private RestTemplate restTemplate;

    RunboardRestClient(Configuration configuration) {
        this.configuration = configuration;
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        factory.setConnectTimeout(1000);
        restTemplate = new RestTemplate();
    }

    public static void main(String[] args) throws Exception {
        new RunboardRestClient(new Configuration()).findNewNotifications();
    }

    @SuppressWarnings("unchecked")
    List<Map<String, Object>> findNewNotifications() throws DispatcherClientException {
        final URI uri = UriComponentsBuilder.fromHttpUrl(configuration.getUrl())
                .path("/runboardNotifications")
                .queryParam("runboardKey", configuration.getRunboardKey())
                .build().toUri();
        try {
            ResponseEntity<List> result = restTemplate.getForEntity(uri, List.class);
            if (result.getStatusCode() == HttpStatus.OK) {
                final List<Map<String, Object>> notifications = (List<Map<String, Object>>) result.getBody();
                notifications.forEach(map -> acknowledge(map.get("id")));
                return (List<Map<String, Object>>) result.getBody();
            } else {
                logger.debug("Unexpected server status code: {}", result.getStatusCode());
                throw new DispatcherClientException(DispatcherClientException.ErrorCode.UNKNOWN_SERVER_ERROR);
            }
        } catch (RestClientException ex) {
            logger.debug("Communication error", ex);
            throw new DispatcherClientException(DispatcherClientException.ErrorCode.COMMUNICATION_ERROR, ex);
        }
    }

    private void acknowledge(Object notificationId) {
        logger.info("Acknowledging notification {}", notificationId);
        final URI uri = UriComponentsBuilder.fromHttpUrl(configuration.getUrl())
                .path("/runboardAcknowledge")
                .queryParam("runboardKey", configuration.getRunboardKey())
                .queryParam("notification", notificationId)
                .build().toUri();
        ResponseEntity<Object> result = restTemplate.getForEntity(uri, Object.class);
        logger.info("Return code: {}", result.getStatusCode());
    }
}
