package net.pkhapps.idispatch.gis.importer;

import net.pkhapps.idispatch.gis.domain.GisDomainConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;

import java.time.Clock;

/**
 * TODO Implement me
 */
@SpringBootApplication
@Import(GisDomainConfiguration.class)
public class ImporterApp {

    public static void main(String[] args) {
        var context = SpringApplication.run(ImporterApp.class, args);
        if (args.length > 0) {
            switch (args[0].toLowerCase()) {
                case "municipalities":
                    context.getBean(MunicipalityImporter.class).importData();
                    break;
                case "road-segments":
                    // TODO Implement me
                    break;
                case "address-points":
                    // TODO Implement me
                    break;
            }
        }
    }

    @Bean
    public Clock clock() {
        return Clock.systemUTC();
    }
}
