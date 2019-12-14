package net.pkhapps.idispatch.core.app;

import io.grpc.Server;
import io.grpc.ServerBuilder;
import net.pkhapps.idispatch.core.app.auth.AuthenticationServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * TODO Document me!
 */
public class CoreServerApp {

    private static final Logger LOGGER = LoggerFactory.getLogger(CoreServerApp.class);
    private final int port;
    private Server server;

    public CoreServerApp(int port) {
        this.port = port;
    }

    public static void main(String[] args) throws Exception {
        var app = new CoreServerApp(5001);
        // TODO Get port from some config file
        // TODO TLS
        app.start();
        app.blockUntilShutdown();
    }

    private synchronized void start() throws IOException {
        if (server != null) {
            throw new IllegalStateException("Server is already running");
        }
        LOGGER.info("Starting server on port {}", port);
        server = ServerBuilder.forPort(port)
                .addService(new AuthenticationServiceImpl())
                .build()
                .start();
        Runtime.getRuntime().addShutdownHook(new Thread(CoreServerApp.this::stop));
        LOGGER.info("Server started");
    }

    private synchronized void stop() {
        if (server != null) {
            LOGGER.info("Stopping server");
            try {
                server.shutdown();
            } finally {
                server = null;
            }
        }
    }

    private synchronized void blockUntilShutdown() throws InterruptedException {
        if (server != null) {
            server.awaitTermination();
        }
    }
}
