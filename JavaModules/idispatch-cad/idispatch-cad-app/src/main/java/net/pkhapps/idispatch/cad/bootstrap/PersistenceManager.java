package net.pkhapps.idispatch.cad.bootstrap;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.pkhapps.idispatch.cad.config.Configuration;
import net.pkhapps.idispatch.cad.config.ConfigurationLoader;
import net.pkhapps.idispatch.cad.config.DefaultConfiguration;
import org.hibernate.cfg.AvailableSettings;
import org.hibernate.hikaricp.internal.HikariCPConnectionProvider;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.ThreadSafe;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.HashMap;

/**
 * The persistence manager is responsible for creating and closing the {@link EntityManagerFactory}.
 */
@ThreadSafe
@Slf4j
class PersistenceManager {

    private static final String PERSITENCE_UNIT_NAME = "idispatch-cad";
    private static final String BASE_NAME = "persistence";
    private final Configuration configuration;
    private final EntityManagerFactory entityManagerFactory;
    /*
     * We use a proxy instead of the real instance to be able to reconfigure or recreate the real instance under
     * the hood without the rest of the application knowing about it.
     */
    private final EntityManagerFactory entityManagerFactoryProxy = (EntityManagerFactory) Proxy.newProxyInstance(
            getClass().getClassLoader(), new Class[]{EntityManagerFactory.class}, this::invokeEntityManager);

    PersistenceManager() {
        configuration = new DefaultConfiguration();
        new ConfigurationLoader(configuration, BASE_NAME).load();
        entityManagerFactory = createEntityManagerFactory();
    }

    /**
     * Returns the {@link EntityManagerFactory}. This instance will not change while the application is running. Do
     * not call {@link EntityManagerFactory#close()} directly on this instance. Instead, use
     * {@link PersistenceManager#close()}.
     */
    @Nonnull
    EntityManagerFactory entityManagerFactory() {
        return entityManagerFactoryProxy;
    }

    private Object invokeEntityManager(Object proxy, Method method, Object[] args) throws Throwable {
        if (method.getName().equals("close")) {
            log.warn("Tried to call close() on EntityManagerFactory proxy, ignoring");
            return null;
        } else {
            return method.invoke(entityManagerFactory, args);
        }
    }

    /**
     * Closes the {@link EntityManagerFactory} and cleans up any other resources.
     */
    void close() {
        log.info("Closing EntityManagerFactory");
        entityManagerFactory.close();
    }

    @Nonnull
    private EntityManagerFactory createEntityManagerFactory() {
        var properties = new HashMap<String, String>();
        properties.put(AvailableSettings.JPA_JDBC_URL,
                configuration.attribute(Attributes.JDBC_URL, String.class));
        properties.put(AvailableSettings.JPA_JDBC_USER,
                configuration.attribute(Attributes.JDBC_USER, String.class));
        properties.put(AvailableSettings.JPA_JDBC_PASSWORD,
                configuration.attribute(Attributes.JDBC_PASSWORD, String.class));
        properties.put(AvailableSettings.CONNECTION_PROVIDER,
                HikariCPConnectionProvider.class.getName());
        properties.put("hibernate.hikari.idleTimeout",
                configuration.attribute(Attributes.IDLE_TIMEOUT_MS, String.class));
        properties.put("hibernate.hikari.connectionTimeout",
                configuration.attribute(Attributes.CONNECTION_TIMEOUT_MS, String.class));
        properties.put(AvailableSettings.HBM2DDL_AUTO, "validate");

        log.info("Creating new EntityManagerFactory");
        return Persistence.createEntityManagerFactory(PERSITENCE_UNIT_NAME, properties);
    }

    @AllArgsConstructor
    enum Attributes implements Configuration.AttributeName {
        JDBC_URL("jdbc.url"),
        JDBC_USER("jdbc.user"),
        JDBC_PASSWORD("jdbc.password"),
        CONNECTION_TIMEOUT_MS("jdbc.connection-pool.connection-timeout-ms"),
        IDLE_TIMEOUT_MS("jdbc.connection-pool.idle-timeout-ms");

        private final String attributeName;

        @Override
        public String toString() {
            return attributeName;
        }
    }
}
