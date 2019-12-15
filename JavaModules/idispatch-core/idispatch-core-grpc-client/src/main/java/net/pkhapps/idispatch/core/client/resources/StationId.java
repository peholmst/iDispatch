package net.pkhapps.idispatch.core.client.resources;

import net.pkhapps.idispatch.core.client.ObjectId;
import org.jetbrains.annotations.NotNull;

/**
 * ID identifying a {@link Station}.
 */
public final class StationId extends ObjectId {

    StationId(@NotNull String uuid) {
        super(uuid);
    }

    StationId(@NotNull net.pkhapps.idispatch.core.grpc.proto.resource.StationId stationId) {
        this(stationId.getUuid());
    }
}
