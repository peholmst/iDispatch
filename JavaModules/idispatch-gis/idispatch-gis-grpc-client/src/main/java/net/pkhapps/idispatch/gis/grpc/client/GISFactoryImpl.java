package net.pkhapps.idispatch.gis.grpc.client;

import io.grpc.ManagedChannelBuilder;
import net.pkhapps.idispatch.gis.api.spi.GIS;
import net.pkhapps.idispatch.gis.api.spi.GISFactory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Properties;

import static java.util.Objects.requireNonNull;

/**
 * TODO Document me
 */
public class GISFactoryImpl implements GISFactory {

    @Override
    public @NotNull GIS createGIS(@Nullable Properties properties) throws Exception {
        requireNonNull(properties, "this implementation requires a Properties instance");
        var channel = ManagedChannelBuilder
                .forAddress(properties.getProperty(PropertyConstants.SERVER_HOST),
                        Integer.parseInt(properties.getProperty(PropertyConstants.SERVER_PORT)))
                .usePlaintext() // TODO Enable SSL?
                .build();
        return new GISImpl(channel);
    }
}
