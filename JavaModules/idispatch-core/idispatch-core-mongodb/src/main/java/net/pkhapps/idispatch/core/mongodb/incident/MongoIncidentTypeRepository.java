package net.pkhapps.idispatch.core.mongodb.incident;

import com.mongodb.client.MongoCollection;
import net.pkhapps.idispatch.core.domain.incident.model.IncidentType;
import net.pkhapps.idispatch.core.domain.incident.model.IncidentTypeId;
import net.pkhapps.idispatch.core.domain.incident.model.IncidentTypeRepository;
import net.pkhapps.idispatch.core.mongodb.common.MongoRepository;
import org.bson.Document;
import org.jetbrains.annotations.NotNull;

/**
 * TODO Document & implement me
 */
public class MongoIncidentTypeRepository extends MongoRepository<IncidentTypeId, IncidentType, IncidentType.Essence>
        implements IncidentTypeRepository {

    /**
     * @param collection
     */
    public MongoIncidentTypeRepository(@NotNull MongoCollection<Document> collection) {
        super(IncidentTypeId.class, IncidentType.class, IncidentType.Essence.class, collection);
    }

    @Override
    protected void copyToDocument(IncidentType.@NotNull Essence source, @NotNull Document destination) {

    }

    @Override
    protected void copyToEssence(@NotNull Document source, IncidentType.@NotNull Essence destination) {

    }
}
