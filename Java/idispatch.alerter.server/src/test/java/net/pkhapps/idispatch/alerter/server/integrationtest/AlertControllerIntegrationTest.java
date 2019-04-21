package net.pkhapps.idispatch.alerter.server.integrationtest;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import net.pkhapps.idispatch.alerter.server.domain.recipient.*;
import net.pkhapps.idispatch.base.domain.OrganizationId;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.messaging.converter.StringMessageConverter;
import org.springframework.messaging.simp.stomp.StompFrameHandler;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;

import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.concurrent.atomic.AtomicReference;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("test")
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AlertControllerIntegrationTest {

    @LocalServerPort
    private int serverPort;
    @Autowired
    private RecipientRepository recipientRepository;

    private RecipientId runboardRIT90;

    @Before
    public void init() {
        runboardRIT90 = recipientRepository.saveAndFlush(new StompRecipient("RIT90 runboard", new OrganizationId(123))
                .setPriority(RecipientPriority.HIGH)
                .addResource(new ResourceCode("RIT901"))
                .addResource(new ResourceCode("RIT903"))
                .addResource(new ResourceCode("RIT906")))
                .getId();

        recipientRepository.saveAndFlush(new SmsRecipient("RIT901 SMS", new OrganizationId(123))
                .setPriority(RecipientPriority.LOW)
                .addPhoneNumber(new PhoneNumber("+358401230901"))
                .addResource(new ResourceCode("RIT901")));
        recipientRepository.saveAndFlush(new StompRecipient("RIT901 mobile", new OrganizationId(123))
                .setPriority(RecipientPriority.NORMAL)
                .addResource(new ResourceCode("RIT901")));

        recipientRepository.saveAndFlush(new StompRecipient("RIT92 runboard", new OrganizationId(123))
                .setPriority(RecipientPriority.HIGH)
                .addResource(new ResourceCode("RIT921"))
                .addResource(new ResourceCode("RIT923")));
        recipientRepository.saveAndFlush(new SmsRecipient("RIT921 SMS", new OrganizationId(123))
                .setPriority(RecipientPriority.LOW)
                .addPhoneNumber(new PhoneNumber("+358401230921"))
                .addResource(new ResourceCode("RIT921")));
        recipientRepository.saveAndFlush(new StompRecipient("RIT921 mobile", new OrganizationId(123))
                .setPriority(RecipientPriority.NORMAL)
                .addResource(new ResourceCode("RIT921")));

        recipientRepository.saveAndFlush(new StompRecipient("RIT93 runboard", new OrganizationId(123))
                .setPriority(RecipientPriority.HIGH)
                .addResource(new ResourceCode("RIT931")));
        recipientRepository.saveAndFlush(new SmsRecipient("RIT931 SMS", new OrganizationId(123))
                .setPriority(RecipientPriority.LOW)
                .addPhoneNumber(new PhoneNumber("+358401230931"))
                .addResource(new ResourceCode("RIT931")));
        recipientRepository.saveAndFlush(new StompRecipient("RIT931 mobile", new OrganizationId(123))
                .setPriority(RecipientPriority.NORMAL)
                .addResource(new ResourceCode("RIT931")));
    }

    @Test
    public void sendAlert() throws Exception {
        // Connect a STOMP client
        var webSocketClient = new StandardWebSocketClient();
        var stompClient = new WebSocketStompClient(webSocketClient);
        stompClient.setMessageConverter(new StringMessageConverter());
        var receivedStompHeaders = new AtomicReference<StompHeaders>();
        var receivedStompPayload = new AtomicReference<String>();
        var sessionHandler = new StompSessionHandlerAdapter() {
            @Override
            public void afterConnected(StompSession session, StompHeaders connectedHeaders) {
                session.subscribe("/alerts/" + runboardRIT90.toString(), new StompFrameHandler() {
                    @Override
                    public Type getPayloadType(StompHeaders headers) {
                        return String.class;
                    }

                    @Override
                    public void handleFrame(StompHeaders headers, Object payload) {
                        receivedStompHeaders.set(headers);
                        receivedStompPayload.set((String) payload);
                    }
                });
            }
        };
        var stompSession = stompClient.connect(getStompUri(), sessionHandler);

        // First send the alert
        var response = RestAssured.given()
                .contentType(ContentType.JSON)
                .body(load("send-alert.json"))
                .when().post(getAlertControllerUri() + "/send");
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getBody().jsonPath().getList("alertedResources")).contains("RIT901", "RIT903", "RIT906", "RIT921", "RIT923", "RIT931");
        assertThat(response.getBody().jsonPath().getInt("alertedResourcesCount")).isEqualTo(6);
        assertThat(response.getBody().jsonPath().getList("unknownResources")).isNull();
        assertThat(response.getBody().jsonPath().getInt("unknownResourcesCount")).isZero();
        var alertId = response.getBody().jsonPath().getLong("alertId");
        assertThat(alertId).isPositive();

        // Then check the status. No resources should be acknowledged yet.
        var statusResponse = RestAssured.given()
                .when().get(getAlertControllerUri() + "/status/{alertId}", alertId);
        assertThat(statusResponse.getStatusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(statusResponse.getBody().jsonPath().getLong("alertId")).isEqualTo(alertId);
        assertThat(statusResponse.getBody().jsonPath().getInt("ackResourcesCount")).isZero();
        assertThat(statusResponse.getBody().jsonPath().getMap("ackResources")).isNull();
        assertThat(statusResponse.getBody().jsonPath().getInt("ackRecipientsCount")).isZero();
        assertThat(statusResponse.getBody().jsonPath().getMap("ackRecipients")).isNull();
        assertThat(statusResponse.getBody().jsonPath().getString("alertDate")).isNotBlank();

        Thread.sleep(500); // Wait for the STOMP client to receive the alert
        assertThat(receivedStompHeaders.get().getFirst("id")).isEqualTo(Long.toString(alertId));
        assertThat(receivedStompHeaders.get().getFirst("contentType")).isEqualTo("IntegrationTestAlert");
        assertThat(receivedStompHeaders.get().getFirst("priority")).isEqualTo("1");

        // Acknowledge one recipient
        var ackResponse = RestAssured.given()
                .formParam("recipientId", runboardRIT90.toString())
                .when().put(getAlertControllerUri() + "/ack/{alertId}", alertId);
        assertThat(ackResponse.getStatusCode()).isEqualTo(HttpStatus.OK.value());

        // Check status again
        statusResponse = RestAssured.given()
                .when().get(getAlertControllerUri() + "/status/{alertId}", alertId);
        assertThat(statusResponse.getStatusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(statusResponse.getBody().jsonPath().getMap("ackResources")).containsKeys("RIT901", "RIT903", "RIT906");
        assertThat(statusResponse.getBody().jsonPath().getInt("ackResourcesCount")).isEqualTo(3);
        assertThat(statusResponse.getBody().jsonPath().getMap("ackRecipients")).containsKeys(runboardRIT90.toString());
        assertThat(statusResponse.getBody().jsonPath().getInt("ackRecipientsCount")).isEqualTo(1);

        // Disconnect
        stompSession.get().disconnect();
    }

    private String getAlertControllerUri() {
        return String.format("http://localhost:%d/idispatch/alerter/api/alert", serverPort);
    }

    private String getStompUri() {
        return String.format("ws://localhost:%d/idispatch/alerter/stomp", serverPort);
    }

    private InputStream load(String fileName) {
        return getClass().getResourceAsStream(fileName);
    }
}
