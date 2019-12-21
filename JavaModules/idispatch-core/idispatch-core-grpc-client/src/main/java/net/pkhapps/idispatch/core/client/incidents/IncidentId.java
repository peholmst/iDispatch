package net.pkhapps.idispatch.core.client.incidents;

import net.pkhapps.idispatch.core.client.ObjectId;
import org.jetbrains.annotations.NotNull;

/**
 * TODO Document me
 */
public class IncidentId extends ObjectId {

    IncidentId(@NotNull String uuid) {
        super(uuid);
    }

    IncidentId(@NotNull net.pkhapps.idispatch.core.grpc.proto.incident.IncidentId incidentId) {
        this(incidentId.getUuid());
    }
}
