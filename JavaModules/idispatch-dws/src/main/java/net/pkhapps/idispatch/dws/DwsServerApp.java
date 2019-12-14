package net.pkhapps.idispatch.dws;

import com.vaadin.flow.server.startup.ServletContextListeners;
import org.eclipse.jetty.annotations.AnnotationConfiguration;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.util.resource.Resource;
import org.eclipse.jetty.webapp.Configuration;
import org.eclipse.jetty.webapp.JettyWebXmlConfiguration;
import org.eclipse.jetty.webapp.WebAppContext;
import org.eclipse.jetty.websocket.jsr356.server.deploy.WebSocketServerContainerInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.MalformedURLException;
import java.net.URI;

/**
 * TODO Document me
 */
public class DwsServerApp {

    private static final Logger LOGGER = LoggerFactory.getLogger(DwsServerApp.class);
    private final int port;
    private Server server;

    public DwsServerApp(int port) {
        this.port = port;
    }

    public static void main(String[] args) throws Exception {
        var app = new DwsServerApp(8080);
        // TODO Get port from some config file
        // TODO TLS
        app.start();
        app.blockUntilShutdown();
    }

    private static Resource findWebRoot() throws MalformedURLException {
        var rootUrl = DwsServerApp.class.getResource("/webapp/ROOT").toString();
        var webRoot = URI.create(rootUrl.substring(0, rootUrl.length() - 5)); // Strip /ROOT from the URL and we get the web root
        LOGGER.debug("Using web root {}", webRoot);
        return Resource.newResource(webRoot);
    }

    private synchronized void start() throws Exception {
        if (server != null) {
            throw new IllegalStateException("Server is already running");
        }
        LOGGER.info("Starting server on port {}", port);

        // TODO Production mode

        var context = new WebAppContext();
        context.setBaseResource(findWebRoot());
        context.setContextPath("/");
        context.setAttribute("org.eclipse.jetty.server.webapp.ContainerIncludeJarPattern", ".*\\.jar|.*/classes/.*");
        context.setConfigurationDiscovered(true);
        context.getServletContext().setExtendedListenerTypes(true);
        context.addEventListener(new ServletContextListeners());
        WebSocketServerContainerInitializer.initialize(context);

        server = new Server(port);
        server.setHandler(context);

        var serverConfiguration = Configuration.ClassList.setServerDefault(server);
        serverConfiguration.addBefore(JettyWebXmlConfiguration.class.getName(), AnnotationConfiguration.class.getName());

        server.start();
        Runtime.getRuntime().addShutdownHook(new Thread(DwsServerApp.this::stop));
        LOGGER.info("Server started");
    }

    private synchronized void stop() {
        if (server != null) {
            LOGGER.info("Stopping server");
            try {
                server.stop();
            } catch (Exception ex) {
                LOGGER.info("Exception while stopping server", ex);
            } finally {
                server = null;
            }
        }
    }

    private synchronized void blockUntilShutdown() throws InterruptedException {
        if (server != null) {
            server.join();
        }
    }
}
