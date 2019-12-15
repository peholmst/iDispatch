package net.pkhapps.idispatch.core.client.resources;

import net.pkhapps.idispatch.core.client.ObjectId;
import org.jetbrains.annotations.NotNull;

/**
 * ID identifying a {@link Resource}.
 */
public final class ResourceId extends ObjectId {

    ResourceId(@NotNull String uuid) {
        super(uuid);
    }

    ResourceId(@NotNull net.pkhapps.idispatch.core.grpc.proto.resource.ResourceId resourceId) {
        this(resourceId.getUuid());
    }
}
