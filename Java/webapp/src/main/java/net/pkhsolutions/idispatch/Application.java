package net.pkhsolutions.idispatch;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.validation.Validation;
import javax.validation.Validator;

/**
 * Main application class
 */
@EnableAutoConfiguration
@EnableJpaRepositories
@EnableTransactionManagement(proxyTargetClass = true)
@EnableAsync
@ComponentScan
@Configuration
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Bean
    Validator validator() {
        return Validation.buildDefaultValidatorFactory().getValidator();
    }
}
