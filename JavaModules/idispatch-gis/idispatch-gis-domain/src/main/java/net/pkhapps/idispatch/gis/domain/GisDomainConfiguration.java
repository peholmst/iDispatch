package net.pkhapps.idispatch.gis.domain;

import net.pkhapps.idispatch.shared.domain.SharedDomainConfiguration;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableJpaRepositories
@EntityScan
@Import(SharedDomainConfiguration.class)
public class GisDomainConfiguration {
}
