package net.pkhapps.idispatch.identity.server.integrationtest;

import io.restassured.RestAssured;
import net.pkhapps.idispatch.identity.server.domain.*;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles({"test", "devel"})
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class OAuthClientIntegrationTest {

    private static final String CLIENT_ID = "test-client";
    private static final String CLIENT_SECRET = "the-secret";
    private static final String USERNAME = "joecool";
    private static final String PASSWORD = "2smart4u";
    private static final String ROLE = "ROLE_TEST_USER";
    private static final String FULL_NAME = "Joe Cool";
    private static final String ORGANIZATION = "Test Organization";

    @LocalServerPort
    private int serverPort;
    @Autowired
    private OrganizationRepository organizationRepository;
    @Autowired
    private ClientRepository clientRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private JwtTokenStore tokenStore;

    private Organization organization;
    private Client client;
    private User user;

    @Before
    public void init() {
        client = clientRepository.findByClientId(CLIENT_ID).orElseGet(() -> {
            var c = new Client(CLIENT_ID);
            c.setClientSecret(passwordEncoder.encode(CLIENT_SECRET));
            c.addGrantType(GrantType.PASSWORD);
            return clientRepository.save(c);
        });

        organization = organizationRepository.findByName(ORGANIZATION).orElseGet(() -> {
            var o = new Organization(ORGANIZATION);
            return organizationRepository.save(o);
        });

        user = userRepository.findByUsername(USERNAME).orElseGet(() -> {
            var u = new User(USERNAME, UserType.INDIVIDUAL, organization);
            u.setFullName(FULL_NAME);
            u.setPassword(passwordEncoder.encode(PASSWORD));
            u.addAuthority(new SimpleGrantedAuthority(ROLE));
            return userRepository.save(u);
        });
    }

    @SuppressWarnings("unchecked")
    @Test
    public void passwordGrant_successfulAuthentication_tokenReturned() {
        var params = new HashMap<String, String>();
        params.put("grant_type", GrantType.PASSWORD.getGrantTypeString());
        params.put("client_id", CLIENT_ID);
        params.put("username", USERNAME);
        params.put("password", PASSWORD);

        var response = RestAssured.given()
                .auth().preemptive().basic(CLIENT_ID, CLIENT_SECRET)
                .and().params(params)
                .when().post(getAccessTokenUri());
        var acessToken = response.jsonPath().getString("access_token");

        var authentication = tokenStore.readAuthentication(acessToken);
        assertThat(authentication.getName()).isEqualTo(USERNAME);
        assertThat(authentication.getAuthorities()).contains(new SimpleGrantedAuthority(ROLE));

        var details = (Map<String, Object>) authentication.getDetails();
        assertThat(details.get("full_name")).isEqualTo(FULL_NAME);
        assertThat(details.get("organization")).isEqualTo(ORGANIZATION);
        assertThat(details.get("organization_id")).isEqualTo(organization.getId().toLong().intValue());
        assertThat(details.get("user_type")).isEqualTo(user.getUserType().name());
        assertThat(details.get("user_id")).isEqualTo(user.getId().toLong().intValue());
    }

    @Test
    public void passwordGrant_failedClientAuthentication_noTokenReturned() {
        var params = new HashMap<String, String>();
        params.put("grant_type", GrantType.PASSWORD.getGrantTypeString());
        params.put("client_id", CLIENT_ID);
        params.put("username", USERNAME);
        params.put("password", PASSWORD);

        var response = RestAssured.given()
                .auth().preemptive().basic(CLIENT_ID, CLIENT_SECRET + "_incorrect")
                .and().params(params)
                .when().post(getAccessTokenUri());
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
    }

    @Test
    public void passwordGrant_failedUserAuthentication_noTokenReturned() {
        var params = new HashMap<String, String>();
        params.put("grant_type", GrantType.PASSWORD.getGrantTypeString());
        params.put("client_id", CLIENT_ID);
        params.put("username", USERNAME);
        params.put("password", PASSWORD + "_incorrect");

        var response = RestAssured.given()
                .auth().preemptive().basic(CLIENT_ID, CLIENT_SECRET)
                .and().params(params)
                .when().post(getAccessTokenUri());
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    private String getAccessTokenUri() {
        return String.format("http://localhost:%d/oauth/token", serverPort);
    }
}
