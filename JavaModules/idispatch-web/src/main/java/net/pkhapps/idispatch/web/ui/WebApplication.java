package net.pkhapps.idispatch.web.ui;

import net.pkhapps.idispatch.application.ApplicationConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;

import java.time.Clock;

/**
 * Entry point into the iDispatch Web Application.
 */
@SpringBootApplication
@Import(ApplicationConfig.class)
public class WebApplication {

    public static void main(String[] args) {
        SpringApplication.run(WebApplication.class, args);
    }

    @Bean
    Clock clock() {
        return Clock.systemUTC();
    }
}
