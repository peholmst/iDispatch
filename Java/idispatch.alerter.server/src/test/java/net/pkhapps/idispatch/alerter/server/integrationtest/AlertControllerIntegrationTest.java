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
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.InputStream;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("test")
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AlertControllerIntegrationTest {

    @LocalServerPort
    private int serverPort;
    @Autowired
    private RecipientRepository recipientRepository;

    @Before
    public void init() {
        recipientRepository.saveAndFlush(new StompRecipient("RIT90 runboard", new OrganizationId(123))
                .setPriority(RecipientPriority.HIGH)
                .addResource(new ResourceCode("RIT901"))
                .addResource(new ResourceCode("RIT903"))
                .addResource(new ResourceCode("RIT906")));
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
    public void sendAlert() {
        // First send the alert
        var response = RestAssured.given()
                .contentType(ContentType.JSON)
                .body(load("send-alert.json"))
                .when().post(getAlertControllerUri() + "/send");
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getBody().jsonPath().getList("alertedResources")).contains("RIT901", "RIT903", "RIT906", "RIT921", "RIT923", "RIT931");
        assertThat(response.getBody().jsonPath().getList("unknownResources")).isEmpty();
        var alertId = response.getBody().jsonPath().getLong("alertId");
        assertThat(alertId).isPositive();

        // Then check the status. No resources should be acknowledged yet.
        var statusResponse = RestAssured.given()
                .when().get(getAlertControllerUri() + "/status/{alertId}", alertId);
        assertThat(statusResponse.getStatusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(statusResponse.getBody().jsonPath().getLong("alertId")).isEqualTo(alertId);
        assertThat(statusResponse.getBody().jsonPath().getList("acknowledgedResources")).isEmpty();
        assertThat(statusResponse.getBody().jsonPath().getList("acknowledgedRecipients")).isEmpty();
        assertThat(statusResponse.getBody().jsonPath().getString("alertTime")).isNotBlank();


        // TODO continue here...
    }

    private String getAlertControllerUri() {
        return String.format("http://localhost:%d/idispatch/alerter/api/alert", serverPort);
    }

    private InputStream load(String fileName) {
        return getClass().getResourceAsStream(fileName);
    }
}
