package net.pkhapps.idispatch.identity.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Main entry point for the iDispatch Identity Server. Put all configurations in the {@code config} package.
 */
@SpringBootApplication
public class IdentityServerApp {

    public static void main(String[] args) {
        SpringApplication.run(IdentityServerApp.class, args);
    }
}
