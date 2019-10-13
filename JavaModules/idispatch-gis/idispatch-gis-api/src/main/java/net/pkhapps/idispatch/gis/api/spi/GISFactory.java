package net.pkhapps.idispatch.gis.api.spi;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Properties;
import java.util.ServiceLoader;

/**
 * TODO Document me
 */
public interface GISFactory {

    static @NotNull GISFactory getInstance() {
        return ServiceLoader.load(GISFactory.class)
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("No GISFactory implementation available"));
    }

    @NotNull GIS createLookupServices(@Nullable Properties properties) throws Exception;
}
