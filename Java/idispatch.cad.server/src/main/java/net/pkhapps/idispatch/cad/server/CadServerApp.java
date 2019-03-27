package net.pkhapps.idispatch.cad.server;

import net.pkhapps.idispatch.base.domain.BaseDomainConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

/**
 * Main entry point for the iDispatch CAD (Computer Aided Dispatch) Server. Put all configurations in the {@code config}
 * package.
 */
@SpringBootApplication
@Import(BaseDomainConfig.class)
public class CadServerApp {

    public static void main(String[] args) {
        SpringApplication.run(CadServerApp.class, args);
    }
}
