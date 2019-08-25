package net.pkhapps.idispatch.core.mongodb.incident;

import com.mongodb.client.MongoDatabase;
import net.pkhapps.idispatch.core.domain.common.IdFactory;
import net.pkhapps.idispatch.core.domain.incident.model.Incident;
import net.pkhapps.idispatch.core.domain.incident.model.IncidentId;
import net.pkhapps.idispatch.core.domain.incident.model.IncidentRepository;
import net.pkhapps.idispatch.core.mongodb.common.MongoRepository;
import org.bson.Document;
import org.jetbrains.annotations.NotNull;

import java.util.stream.Stream;

/**
 * Implementation of {@link net.pkhapps.idispatch.core.domain.incident.model.IncidentRepository} that uses MongoDB
 * to store the aggregates. Also acts as an {@link IdFactory} for {@link IncidentId}s.
 */
public class MongoIncidentRepository extends MongoRepository<IncidentId, Incident, Incident.Essence>
        implements IncidentRepository, IdFactory<IncidentId> {

    public MongoIncidentRepository(MongoDatabase mongoDatabase) {
        super(IncidentId.class, Incident.class, Incident.Essence.class, mongoDatabase.getCollection("incident"));
        // TODO Create indexes etc.
    }

    @Override
    protected void copyToDocument(Incident.@NotNull Essence source, @NotNull Document destination) {
        // TODO Implement me
    }

    @Override
    protected void copyToEssence(@NotNull Document source, @NotNull Incident.Essence destination) {
        // TODO Implement me
    }

    @Override
    public @NotNull Stream<Incident> findByParent(@NotNull IncidentId parent) {
        // TODO Implement findByParent
        throw new UnsupportedOperationException("Not implemented yet");
    }
}
