package net.pkhapps.idispatch.cad.config;

import lombok.extern.slf4j.Slf4j;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.ThreadSafe;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.*;

/**
 * Utility class for loading configuration attributes from {@link Properties} files and using them to populate a
 * {@link Configuration} object. The files are expected to be located in the root of the class path.
 * <p>
 * Different files can be loaded for different profiles by appending the <code>-[profile name]</code> suffix to the base
 * name of the property file. Thus, if the base name is {@code application}, the default properties would be loaded from
 * {@code application.properties} and the properties for the {@code production} profile from
 * {@code application-production.properties}.
 * <p>
 * The default properties will be loaded first and they can then be overridden by profile properties. The order in which
 * profile properties are loaded is undefined.
 */
@Slf4j
@ThreadSafe
public class ConfigurationLoader {

    private final Configuration configuration;
    private final String baseName;

    /**
     * Creates a new {@code ConfigurationLoader}.
     *
     * @param configuration the configuration object to load.
     * @param baseName      the base name of the configuration file, without the {@code .properties} suffix.
     */
    public ConfigurationLoader(@Nonnull Configuration configuration, @Nonnull String baseName) {
        this.configuration = Objects.requireNonNull(configuration, "configuration must not be null");
        this.baseName = Objects.requireNonNull(baseName, "baseName must not be null");
    }

    /**
     * Loads the attributes from the properties files and {@link Configuration#setMultiple(Map) populates} the
     * configuration object with them.
     */
    public void load() {
        configurationFileNameVariants().forEach(this::loadFromClassPath);
    }

    private List<String> configurationFileNameVariants() {
        var names = new ArrayList<String>();
        names.add(String.format("/%s.properties", baseName));
        Profile.activeProfiles().forEach(profile -> names.add(String.format("/%s-%s.properties", baseName, profile)));
        return names;
    }

    private void loadFromClassPath(@Nonnull String fileName) {
        var resource = getClass().getResource(fileName);
        if (resource != null) {
            log.debug("Loading from configuration file {}", fileName);
            try (InputStream stream = resource.openStream();
                 InputStreamReader reader = new InputStreamReader(stream, StandardCharsets.UTF_8)) {
                var properties = new Properties();
                properties.load(reader);
                configuration.setMultiple(properties);
            } catch (IOException ex) {
                throw new IllegalStateException("Could not load configuration file " + fileName, ex);
            }
        } else {
            log.debug("Configuration file {} did not exist on classpath", fileName);
        }
    }
}
