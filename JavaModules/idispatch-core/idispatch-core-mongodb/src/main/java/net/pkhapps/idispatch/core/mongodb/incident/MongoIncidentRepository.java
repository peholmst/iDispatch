package net.pkhapps.idispatch.core.mongodb.incident;

import com.mongodb.client.MongoDatabase;
import net.pkhapps.idispatch.core.domain.common.IdFactory;
import net.pkhapps.idispatch.core.domain.common.PhoneNumber;
import net.pkhapps.idispatch.core.domain.incident.model.*;
import net.pkhapps.idispatch.core.mongodb.common.MongoRepository;
import net.pkhapps.idispatch.core.mongodb.geo.LocationMapper;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.jetbrains.annotations.NotNull;

import java.time.Instant;
import java.util.stream.Stream;

/**
 * Implementation of {@link net.pkhapps.idispatch.core.domain.incident.model.IncidentRepository} that uses MongoDB
 * to store the aggregates. Also acts as an {@link IdFactory} for {@link IncidentId}s.
 */
public class MongoIncidentRepository extends MongoRepository<IncidentId, Incident, Incident.Essence>
        implements IncidentRepository {

    private static final String KEY_OPENED_ON = "openedOn";
    private static final String KEY_STATE = "state";
    private static final String KEY_LOCATION = "location";
    private static final String KEY_TYPE = "type";
    private static final String KEY_PRIORITY = "priority";
    private static final String KEY_ON_HOLD_REASON = "onHoldReason";
    private static final String KEY_CLOSED_ON = "closedOn";
    private static final String KEY_DETAILS = "details";
    private static final String KEY_INFORMER = "informer";
    private static final String KEY_NAME = "name";
    private static final String KEY_PHONE = "phone";

    private final LocationMapper locationMapper = new LocationMapper();

    public MongoIncidentRepository(MongoDatabase mongoDatabase) {
        super(IncidentId.class, Incident.class, Incident.Essence.class, mongoDatabase.getCollection("incident"));
        // TODO Create indexes etc.
    }

    @Override
    protected void copyToDocument(@NotNull Incident.Essence source, @NotNull Document destination) {
        destination.append(KEY_OPENED_ON, source.getOpenedOn());
        destination.append(KEY_STATE, source.getState());
        destination.append(KEY_LOCATION, locationMapper.toDocument(source.getLocation()));
        destination.append(KEY_TYPE, IncidentTypeId.unwrap(source.getType(), ObjectId.class));
        destination.append(KEY_PRIORITY, source.getPriority());
        destination.append(KEY_ON_HOLD_REASON, source.getOnHoldReason());
        destination.append(KEY_CLOSED_ON, source.getClosedOn());
        destination.append(KEY_DETAILS, source.getDetails());
        destination.append(KEY_INFORMER, new Document(KEY_NAME, source.getInformerName())
                .append(KEY_PHONE, source.getInformerPhoneNumber()));
    }

    @Override
    protected void copyToEssence(@NotNull Document source, @NotNull Incident.Essence destination) {
        destination.setOpenedOn(source.get(KEY_OPENED_ON, Instant.class));
        destination.setState(source.get(KEY_STATE, IncidentState.class));
        destination.setLocation(locationMapper.toLocation(source.get(KEY_LOCATION, Document.class)));
        destination.setType(IncidentTypeId.wrap(IncidentTypeId.class, source.getObjectId(KEY_TYPE)));
        destination.setPriority(source.get(KEY_PRIORITY, IncidentPriority.class));
        destination.setOnHoldReason(source.getString(KEY_ON_HOLD_REASON));
        destination.setClosedOn(source.get(KEY_CLOSED_ON, Instant.class));
        destination.setDetails(source.getString(KEY_DETAILS));
        destination.setInformerName(source.get(KEY_INFORMER, Document.class).getString(KEY_NAME));
        destination.setInformerPhoneNumber(source.get(KEY_INFORMER, Document.class).get(KEY_PHONE, PhoneNumber.class));
    }

    @Override
    public @NotNull Stream<Incident> findByParent(@NotNull IncidentId parent) {
        // TODO Implement findByParent
        throw new UnsupportedOperationException("Not implemented yet");
    }
}
