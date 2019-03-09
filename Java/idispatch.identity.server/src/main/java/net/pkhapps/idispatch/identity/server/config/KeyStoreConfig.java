package net.pkhapps.idispatch.identity.server.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.File;
import java.security.KeyStore;

/**
 * Configuration for the Java Key Store used by this application.
 */
@Configuration
@Slf4j
class KeyStoreConfig {

    @Value("${application.key-store.location}")
    private File keystoreLocation;
    @Value("${application.key-store.password}")
    private String keystorePassword;

    @Bean
    public KeyStore keyStore() throws Exception {
        log.info("Using key store in file {}", keystoreLocation);
        return KeyStore.getInstance(keystoreLocation, keystorePassword.toCharArray());
    }
}
