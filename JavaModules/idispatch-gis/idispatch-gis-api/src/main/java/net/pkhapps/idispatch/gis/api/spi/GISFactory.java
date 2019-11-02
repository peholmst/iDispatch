package net.pkhapps.idispatch.gis.api.spi;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Properties;
import java.util.ServiceLoader;

/**
 * SPI for creating new {@link GIS} instances. Implementations of this interface should register themselves using
 * the Java {@linkplain ServiceLoader service loader}. In practice, this means creating a file named
 * {@code META-INF/net.pkhapps.idispatch.gis.api.spi.GISFactory} and putting the name of the implementation class into
 * the file.
 */
@SuppressWarnings("SpellCheckingInspection")
public interface GISFactory {

    /**
     * Fetches an implementation of {@link GISFactory} using the service loader. If there are more than one
     * implementation to choose from, the first one will be selected.
     *
     * @return the {@link GISFactory}
     * @throws IllegalStateException if no implementations are available
     */
    static @NotNull GISFactory getInstance() {
        return ServiceLoader.load(GISFactory.class)
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("No GISFactory implementation available"));
    }

    /**
     * Creates a new {@link GIS} instance, optionally passing configuration properties to the factory. The contents
     * of these properties are defined by the implementation.
     *
     * @param properties any configuration properties
     * @return a new instance of {@link GIS}
     * @throws Exception if something went wrong while creating the instance
     */
    @NotNull GIS createGIS(@Nullable Properties properties) throws Exception;
}
