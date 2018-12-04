package net.pkhapps.idispatch.gis.importer;

import net.pkhapps.idispatch.gis.domain.GisDomainConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;

import java.io.File;
import java.time.Clock;
import java.util.Arrays;

/**
 * Java command line application for importing map data into the database. It is not intended for end users and so
 * the UX is pretty rough and the best documentation is the source code.
 */
@SpringBootApplication
@Import(GisDomainConfiguration.class)
public class ImporterApp {

    public static void main(String[] args) {
        var context = SpringApplication.run(ImporterApp.class, args);
        var argumentIterator = Arrays.asList(args).iterator();
        while (argumentIterator.hasNext()) {
            var arg = argumentIterator.next();
            if (arg.equals("municipalities")) {
                context.getBean(MunicipalityImporter.class).importData();
            } else if (arg.equals("terrain")) {
                var path = new File(argumentIterator.next());
                context.getBean(TerrainDatabaseImporter.class).importData(path);
            }
        }
    }

    @Bean
    public Clock clock() {
        return Clock.systemUTC();
    }
}
