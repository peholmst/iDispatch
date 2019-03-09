package net.pkhapps.idispatch.identity.server.config;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.File;
import java.security.KeyStore;

/**
 * Configuration for the Java Key Store used by this application.
 */
@Configuration
class KeyStoreConfig {

    @Value("${application.key-store.location}")
    private File keystoreLocation;
    @Value("${application.key-store.password}")
    private String keystorePassword;

    @Bean
    public KeyStore keyStore() throws Exception {
        LoggerFactory.getLogger(getClass()).info("Using key store in file {}", keystoreLocation);
        return KeyStore.getInstance(keystoreLocation, keystorePassword.toCharArray());
    }
}
