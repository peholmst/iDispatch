package net.pkhapps.idispatch.core.client.resourcestatus;

import net.pkhapps.idispatch.core.client.ObjectId;
import org.jetbrains.annotations.NotNull;

/**
 * ID for a {@link ResourceState}.
 */
public final class ResourceStateId extends ObjectId {

    ResourceStateId(@NotNull String uuid) {
        super(uuid);
    }

    ResourceStateId(@NotNull net.pkhapps.idispatch.core.grpc.proto.resourcestatus.ResourceStateId resourceStateId) {
        this(resourceStateId.getUuid());
    }
}
