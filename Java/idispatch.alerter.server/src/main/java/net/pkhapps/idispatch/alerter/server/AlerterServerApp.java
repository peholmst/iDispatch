package net.pkhapps.idispatch.alerter.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * Main entry point for the iDispatch Alerter Server. Put all configurations in the {@code config} package.
 */
@SpringBootApplication
@EnableAsync
public class AlerterServerApp {

    public static void main(String[] args) {
        SpringApplication.run(AlerterServerApp.class, args);
    }
}
