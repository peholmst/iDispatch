package net.pkhapps.idispatch.dws;

import net.pkhapps.idispatch.gis.api.lookup.LocationFeatureLookupService;
import net.pkhapps.idispatch.gis.api.lookup.MunicipalityLookupService;
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
public class Bootstrapper implements ServletContextListener, Services {

    private static final Logger LOGGER = LoggerFactory.getLogger(Bootstrapper.class);

    private static Services SERVICES;

    private GIS gis;

    public static @NotNull Services services() {
        if (SERVICES == null) {
            throw new IllegalStateException("No Services available");
        }
        return SERVICES;
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

        SERVICES = this;

        System.setProperty("vaadin.i18n.provider", ResourceBundleI18NProvider.class.getName());
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
        SERVICES = null;
    }

    @Override
    public @NotNull MunicipalityLookupService getMunicipalityLookupService() {
        return gis.getMunicipalityLookupService();
    }

    @Override
    public @NotNull LocationFeatureLookupService getLocationFeatureLookupService() {
        return gis.getLocationFeatureLookupService();
    }
}
