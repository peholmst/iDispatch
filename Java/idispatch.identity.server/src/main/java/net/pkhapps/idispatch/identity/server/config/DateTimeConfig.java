package net.pkhapps.idispatch.identity.server.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Clock;

/**
 * Configuration related to date and time.
 */
@Configuration
class DateTimeConfig {

    @Bean
    public Clock clock() {
        return Clock.systemUTC();
    }
}
