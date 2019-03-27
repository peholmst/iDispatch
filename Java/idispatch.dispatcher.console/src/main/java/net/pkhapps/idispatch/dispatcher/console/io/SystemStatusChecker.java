package net.pkhapps.idispatch.dispatcher.console.io;

import javafx.application.Platform;
import javafx.concurrent.ScheduledService;
import javafx.concurrent.Task;
import javafx.util.Duration;
import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.client.ClientProperties;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.Response;
import java.net.URI;

import static java.util.Objects.requireNonNull;

/**
 * TODO Document me!
 */
public class SystemStatusChecker {

    private final static Logger LOGGER = LoggerFactory.getLogger(SystemStatusChecker.class);
    private static final int CONNECT_TIMEOUT_MS = 500;
    private static final int READ_TIMEOUT_MS = 500;
    private static final int PING_INTERVAL_S = 15;
    private final Client client;
    private final ScheduledService<Void> pingService;

    public SystemStatusChecker(@NotNull Server server, @NotNull SystemStatusSummary systemStatusSummary) {
        requireNonNull(systemStatusSummary);
        requireNonNull(server);

        var clientConfig = new ClientConfig();
        clientConfig.property(ClientProperties.CONNECT_TIMEOUT, CONNECT_TIMEOUT_MS);
        clientConfig.property(ClientProperties.READ_TIMEOUT, READ_TIMEOUT_MS);
        client = ClientBuilder.newClient(clientConfig);

        pingService = new ScheduledService<>() {
            @Override
            protected Task<Void> createTask() {
                return new Task<>() {
                    @Override
                    protected Void call() {
                        var gis = ping(server.getGisServerUri()) ? SystemStatus.State.ONLINE : SystemStatus.State.OFFLINE;
                        var cad = ping(server.getCadServerUri()) ? SystemStatus.State.ONLINE : SystemStatus.State.OFFLINE;
                        var identity = ping(server.getIdentityServerUri()) ? SystemStatus.State.ONLINE : SystemStatus.State.OFFLINE;

                        Platform.runLater(() -> {
                            systemStatusSummary.getGisServer().setState(gis);
                            systemStatusSummary.getCadServer().setState(cad);
                            systemStatusSummary.getIdentityServer().setState(identity);
                        });

                        return null;
                    }
                };
            }
        };
        pingService.setPeriod(Duration.seconds(PING_INTERVAL_S));
    }

    public void start() {
        pingService.start();
    }

    public void cancel() {
        pingService.cancel();
    }

    private boolean ping(@NotNull URI uri) {
        try {
            var response = client.target(uri).path("/ping").request().get();
            LOGGER.debug("Pinging {} returned status {}", uri, response.getStatus());
            return response.getStatusInfo().getFamily() == Response.Status.Family.SUCCESSFUL;
        } catch (Exception ex) {
            LOGGER.error("Error while pinging " + uri, ex);
            return false;
        }
    }
}
