package net.pkhapps.idispatch.gis.grpc.server;

import io.grpc.ServerBuilder;
import net.pkhapps.idispatch.gis.api.spi.GISFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.util.Properties;

/**
 * TODO Document me
 */
public class GISServer {

    private static final Logger LOGGER = LoggerFactory.getLogger(GISServer.class);

    public static void main(String[] args) throws Exception {
        var properties = new Properties();
        var configurationFilePath = System.getProperty("config.file", "settings.properties");
        var configurationFile = new File(configurationFilePath);
        if (configurationFile.exists()) {
            LOGGER.info("Loading configuration from {}", configurationFilePath);
            try (var inputStream = new FileInputStream(configurationFile)) {
                properties.load(inputStream);
            }
        } else {
            LOGGER.error("Configuration file {} does not exist", configurationFilePath);
        }

        var port = Integer.parseInt(properties.getProperty("server.port", "9090"));
        LOGGER.info("Using server port {}", port);
        var gis = GISFactory.getInstance().createGIS(properties);
        var server = ServerBuilder.forPort(port)
                .addService(new MunicipalityLookupServiceImpl(gis.getMunicipalityLookupService()))
                .addService(new LocationFeatureLookupServiceImpl(gis.getLocationFeatureLookupService()))
                .build();
        server.start();
        server.awaitTermination();
    }
}
