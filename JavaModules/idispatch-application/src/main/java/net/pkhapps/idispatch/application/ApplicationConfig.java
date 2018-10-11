package net.pkhapps.idispatch.application;

import net.pkhapps.idispatch.domain.DomainConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@ComponentScan
@Import(DomainConfiguration.class)
public class ApplicationConfig {

    
}
