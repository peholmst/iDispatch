package net.pkhapps.idispatch.domain;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * Spring configuration for the iDispatch Domain Model module.
 */
@Configuration
@ComponentScan
@EnableJpaRepositories(basePackages = {"net.pkhapps.idispatch.domain",
        "org.springframework.data.jpa.convert.threeten"})
public class DomainConfiguration {
}
