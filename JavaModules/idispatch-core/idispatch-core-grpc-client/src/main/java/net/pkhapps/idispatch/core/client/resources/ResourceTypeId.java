package net.pkhapps.idispatch.core.client.resources;

import net.pkhapps.idispatch.core.client.ObjectId;
import org.jetbrains.annotations.NotNull;

/**
 * ID identifying a {@link ResourceType}.
 */
public final class ResourceTypeId extends ObjectId {

    ResourceTypeId(@NotNull String uuid) {
        super(uuid);
    }

    ResourceTypeId(@NotNull net.pkhapps.idispatch.core.grpc.proto.resource.ResourceTypeId resourceTypeId) {
        this(resourceTypeId.getUuid());
    }
}
