package net.pkhsolutions.idispatch.runboard.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

public class Configuration {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private Properties properties;

    protected Configuration() {
        final File file = new File(System.getProperty("idispatch.configFile", "runboard.properties"));
        if (!file.exists()) {
            logger.warn("File {} not found. Will use system properties instead.", file.getAbsolutePath());
        } else {
            logger.info("Attempting to read properties from {}", file.getAbsolutePath());
            properties = new Properties();
            try (FileReader reader = new FileReader(file)) {
                properties.load(reader);
            } catch (IOException ex) {
                logger.error("Could not read properties from file", ex);
            }
        }
    }

    protected String getProperty(String property, String defaultValue) {
        String value = null;
        if (properties != null) {
            value = properties.getProperty(property);
        }
        if (value == null) {
            value = System.getProperty(property);
        }
        if (value == null) {
            return defaultValue;
        } else {
            return value;
        }
    }

    public String getUrl() {
        return getProperty("idispatch.rest.url", "http://localhost:8080/rest");
    }

    public String getRunboardKey() {
        return getProperty("idispatch.runboardKey", "test123");
    }

    public int getPollingIntervalMilliseconds() {
        return Integer.parseInt(getProperty("idispatch.pollingInterval", "1000"));
    }

}
