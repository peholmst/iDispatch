package net.pkhapps.idispatch.dws;

import net.pkhapps.idispatch.gis.api.spi.GIS;
import net.pkhapps.idispatch.gis.api.spi.GISFactory;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import java.io.IOException;
import java.util.Properties;

/**
 * TODO Document me
 */
@WebListener
public class Services implements ServletContextListener {

    private static final Logger LOGGER = LoggerFactory.getLogger(Services.class);

    private static Services INSTANCE;

    private GIS gis;

    Services() {
        INSTANCE = this;
    }

    public static @NotNull Services getInstance() {
        return INSTANCE;
    }

    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        final var properties = new Properties();
        try {
            properties.load(servletContextEvent.getServletContext()
                    .getResourceAsStream("/WEB-INF/config.properties"));
        } catch (IOException ex) {
            LOGGER.error("Error reading the configuration file", ex);
        }

        try {
            gis = GISFactory.getInstance().createGIS(properties);
        } catch (Exception ex) {
            LOGGER.error("Error setting up the GIS subsystem", ex);
            throw new RuntimeException(ex);
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {
        if (gis instanceof AutoCloseable) {
            try {
                ((AutoCloseable) gis).close();
            } catch (Exception ex) {
                LOGGER.error("Error closing the GIS subsystem", ex);
            }
        }
    }
}
