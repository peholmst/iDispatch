package net.pkhapps.idispatch.dispatcher.console.io.identity;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.pkhapps.idispatch.dispatcher.console.io.Server;
import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.client.ClientProperties;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.jwt.JwtHelper;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Form;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Base64;
import java.util.HashSet;

import static java.util.Objects.requireNonNull;

/**
 * TODO Document me!
 */
public class IdentityServerClient {

    private static final Logger LOGGER = LoggerFactory.getLogger(IdentityServerClient.class);
    private static final int CONNECT_TIMEOUT_MS = 1000;
    private static final int READ_TIMEOUT_MS = 1000;

    private final Server server;
    private final WebTarget loginTarget;

    public IdentityServerClient(@NotNull Server server) {
        this.server = requireNonNull(server);

        var clientConfig = new ClientConfig();
        clientConfig.property(ClientProperties.CONNECT_TIMEOUT, CONNECT_TIMEOUT_MS);
        clientConfig.property(ClientProperties.READ_TIMEOUT, READ_TIMEOUT_MS);
        Client client = ClientBuilder.newClient(clientConfig);
        loginTarget = client.target(server.getIdentityServerUri()).path("/oauth/token");
        LOGGER.info("Using login URI [{}]", loginTarget.getUri());
    }

    public @NotNull User login(@NotNull String username, @NotNull String password) throws LoginException {
        var authHeaderValue = "Basic " + Base64.getEncoder().encodeToString((server.getClientId() + ":" + server.getClientSecret()).getBytes());
        LOGGER.debug("Authentication header value: [{}]", authHeaderValue);
        var loginForm = new Form();
        loginForm.param("grant_type", "password");
        loginForm.param("client_id", server.getClientId());
        loginForm.param("username", username);
        loginForm.param("password", password);
        LOGGER.debug("Attempting to login using username [{}] and client ID [{}]", username, server.getClientId());
        try {
            var response = loginTarget.request()
                    .header(HttpHeaders.AUTHORIZATION, authHeaderValue)
                    .post(Entity.entity(loginForm, MediaType.APPLICATION_FORM_URLENCODED_TYPE));
            var status = response.getStatus();
            LOGGER.debug("Response status was [{}]", status);
            if (status == Response.Status.UNAUTHORIZED.getStatusCode()) {
                throw new LoginException(LoginException.ErrorType.INVALID_CLIENT_CREDENTIALS);
            } else if (status == Response.Status.BAD_REQUEST.getStatusCode()) {
                throw new LoginException(LoginException.ErrorType.INVALID_USER_CREDENTALS);
            }
            // TODO Extract response
            var entity = response.readEntity(JsonNode.class);
            LOGGER.debug("Response: [{}]", entity);

            var accessToken = entity.get("access_token").asText();
            var jwt = JwtHelper.decode(accessToken);
            LOGGER.debug("JWT: [{}]", jwt);

            var claims = new ObjectMapper().readTree(jwt.getClaims());
            var fullName = claims.get("full_name").asText();
            var organization = claims.get("organization").asText();
            var authorities = new HashSet<String>();
            for (var authority : claims.get("authorities")) {
                authorities.add(authority.asText());
            }
            return new User(fullName, organization, accessToken, authorities);
        } catch (Exception ex) {
            if (ex instanceof LoginException) {
                throw (LoginException) ex;
            }
            LOGGER.error("Error while logging in", ex);
            throw new LoginException(LoginException.ErrorType.COMMUNICATION_ERROR);
        }
    }
}
