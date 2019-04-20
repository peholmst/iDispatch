package net.pkhapps.idispatch.alerter.server.domain;

import net.pkhapps.idispatch.base.domain.BaseDomainConfig;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@Import(BaseDomainConfig.class)
@EnableJpaRepositories
@EntityScan
class AlerterDomainConfig {
}
