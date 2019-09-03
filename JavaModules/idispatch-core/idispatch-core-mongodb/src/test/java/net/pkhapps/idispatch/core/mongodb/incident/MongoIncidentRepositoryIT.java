package net.pkhapps.idispatch.core.mongodb.incident;

import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import net.pkhapps.idispatch.core.domain.common.DefaultDomainContext;
import net.pkhapps.idispatch.core.domain.common.DomainContextHolder;
import net.pkhapps.idispatch.core.domain.common.SingletonDomainContextProvider;
import net.pkhapps.idispatch.core.domain.incident.model.IncidentFactory;
import net.pkhapps.idispatch.core.domain.incident.model.IncidentPriority;
import net.pkhapps.idispatch.core.domain.incident.model.IncidentState;
import net.pkhapps.idispatch.core.mongodb.common.EnumCodec;
import org.bson.codecs.configuration.CodecRegistries;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

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
    public void saveFullBlownIncident() {
        var incident = factory.createIncident();

    }
}
