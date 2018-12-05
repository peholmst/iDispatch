package net.pkhapps.idispatch.shared.domain;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import java.time.Clock;

@Configuration
@EnableJpaRepositories
@EntityScan
public class SharedDomainConfiguration {

    @Bean
    public Clock systemUTCClock() {
        return Clock.systemUTC();
    }
}
