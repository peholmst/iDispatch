package net.pkhapps.idispatch.core.mongodb.incident;

import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import net.pkhapps.idispatch.core.domain.common.DefaultDomainContext;
import net.pkhapps.idispatch.core.domain.common.DomainContextHolder;
import net.pkhapps.idispatch.core.domain.common.SingletonDomainContextProvider;
import net.pkhapps.idispatch.core.domain.geo.*;
import net.pkhapps.idispatch.core.domain.i18n.Locales;
import net.pkhapps.idispatch.core.domain.i18n.MultilingualString;
import net.pkhapps.idispatch.core.domain.incident.model.IncidentFactory;
import net.pkhapps.idispatch.core.domain.incident.model.IncidentPriority;
import net.pkhapps.idispatch.core.domain.incident.model.IncidentState;
import net.pkhapps.idispatch.core.mongodb.common.EnumCodec;
import org.bson.codecs.configuration.CodecRegistries;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Integration test for {@link MongoIncidentRepository}.
 */
public class MongoIncidentRepositoryIT {

    private MongoClient mongoClient;
    private IncidentFactory factory;
    private MongoIncidentRepository repository;

    @BeforeTest
    public void setUpTest() {
        // TODO This feels like it could be done better
        DomainContextHolder.setProvider(new SingletonDomainContextProvider(new DefaultDomainContext()));

        mongoClient = MongoClients.create();
        var codecRegistry = CodecRegistries.fromCodecs(
                new EnumCodec<>(IncidentState.class),
                new EnumCodec<>(IncidentPriority.class)

        );
        var database = mongoClient.getDatabase("idispatch_core_integration_test")
                .withCodecRegistry(CodecRegistries.fromRegistries(codecRegistry,
                        MongoClientSettings.getDefaultCodecRegistry()));

        repository = new MongoIncidentRepository(database);
        factory = IncidentFactory.createDefault(repository);
    }

    @Test
    public void saveEmptyIncident() {
        var incident = factory.createIncident();
        var saved = repository.save(incident);

        assertThat(saved).isNotSameAs(incident);

        assertThat(incident.optLockVersion()).isEqualTo(0);
        assertThat(incident.createdOn()).isEmpty();
        assertThat(incident.lastModifiedOn()).isEmpty();

        assertThat(saved.optLockVersion()).isEqualTo(1);
        assertThat(saved.createdOn()).isNotEmpty();
        assertThat(saved.lastModifiedOn()).isEqualTo(saved.createdOn());
    }

    @Test
    public void saveIncidentAtRoadLocation() {
        var incident = factory.createIncident();
        incident.pinpoint(new RoadLocation(
                Position.createFromTm35Fin(239710, 6694462),
                new MunicipalityId("445"),
                "Mellan Ålövägen och Pajbackavägen",
                new MultilingualString(Map.of(Locales.SWEDISH, "Skärgårdsvägen",
                        Locales.FINNISH, "Saaristotie"))));
        var saved = repository.save(incident);

        assertThat(saved.location()).isEqualTo(incident.location());
        assertThat(saved.location()).isNotSameAs(incident.location());
    }

    @Test
    public void saveIncidentAtIntersection() {
        var incident = factory.createIncident();
        incident.pinpoint(new Intersection(
                Position.createFromTm35Fin(236071, 6689793),
                new MunicipalityId("445"),
                "Mitt i korsningen",
                new MultilingualString(Map.of(Locales.SWEDISH, "Skärgårdsvägen",
                        Locales.FINNISH, "Saaristotie")),
                new MultilingualString(Map.of(Locales.SWEDISH, "Airistovägen",
                        Locales.FINNISH, "Airistontie"))
        ));
        var saved = repository.save(incident);

        assertThat(saved.location()).isEqualTo(incident.location());
        assertThat(saved.location()).isNotSameAs(incident.location());
    }

    @Test
    public void saveIncidentAtStreetAddress() {
        var incident = factory.createIncident();
        incident.pinpoint(new StreetAddress(
                Position.createFromTm35Fin(241725, 6694031),
                new MunicipalityId("445"),
                "Den här lägenheten finns inte på riktigt",
                new MultilingualString(Map.of(Locales.SWEDISH, "Tingstigen",
                        Locales.FINNISH, "Käräjäpolku")),
                "4", "99"
        ));
        var saved = repository.save(incident);

        assertThat(saved.location()).isEqualTo(incident.location());
        assertThat(saved.location()).isNotSameAs(incident.location());
    }

    @Test
    public void saveIncidentAtUnnamedLocation() {
        var incident = factory.createIncident();
        incident.pinpoint(new UnnamedLocation(
                Position.createFromTm35Fin(241337, 6693729),
                new MunicipalityId("445"),
                "Naturstigen"
        ));
        var saved = repository.save(incident);

        assertThat(saved.location()).isEqualTo(incident.location());
        assertThat(saved.location()).isNotSameAs(incident.location());
    }
}
